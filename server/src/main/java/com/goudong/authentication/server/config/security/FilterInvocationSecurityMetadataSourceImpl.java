package com.goudong.authentication.server.config.security;

import com.goudong.authentication.common.util.CollectionUtil;
import com.goudong.authentication.server.constant.CommonConst;
import com.goudong.authentication.server.service.dto.ApiPermissionDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseMenuManagerService;
import com.goudong.authentication.server.util.SecurityContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 根据请求方法和请求路径，获取该次请求必须拥有的角色权限。
 * @author chenf
 */
@Slf4j
@Component
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    /**
     * 应用管理服务接口
     */
    @Resource
    private BaseMenuManagerService baseMenuManagerService;

    /**
     * 获取请求资源需要的权限，如果返回非空集合，就需要执行{@link AccessDecisionManagerImpl#decide(Authentication, Object, Collection)} 方法继续进行鉴权。
     * @param o 请求对象
     * @return 资源需要的权限集合
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) {
        // 获取上下文路径
        String contextPath = ((FilterInvocation) o).getHttpRequest().getContextPath();
        // 获取请求地址，不包含上下文（server.servlet.context-path），这样方便迁移。
        String requestUrl = ((FilterInvocation) o).getRequestUrl();

        if(this.isIgnoreUrl(contextPath, requestUrl)){
            return null;
        }

        // 获取请求的方法
        String requestMethod = ((FilterInvocation) o).getHttpRequest().getMethod().toUpperCase();
        log.debug("requestUrl >> {}，requestMethod >> {}", requestUrl, requestMethod);

        MyAuthentication myAuthentication = SecurityContextUtil.get();
        // 资源需要的权限集合，如果是空集合就不需要校验权限。
        Set<ConfigAttribute> set = new HashSet<>();
        // 不是超级管理员，不是管理员 就需要校验权限
        if (!myAuthentication.admin()) {
            // 查询指定应用，包含指定请求方式
            List<ApiPermissionDTO> apiPermissionDTOS = baseMenuManagerService.listApiPermissionByAppId(myAuthentication.getRealAppId());
            if (!Objects.equals(CommonConst.AUTHENTICATION_SERVER_APP_ID, myAuthentication.getRealAppId())) {
                log.debug("查询认证服务的相关权限");
                apiPermissionDTOS.addAll(baseMenuManagerService.listApiPermissionByAppId(CommonConst.AUTHENTICATION_SERVER_APP_ID));
            }
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            for (ApiPermissionDTO permissionDTO : apiPermissionDTOS) {
                if (antPathMatcher.match(permissionDTO.getPath(), requestUrl) && permissionDTO.getMethod().contains(requestMethod)) {
                    if (CollectionUtil.isNotEmpty(permissionDTO.getRoles())) {
                        set.addAll(permissionDTO.getRoles().stream().map(SecurityConfig::new).collect(Collectors.toList()));
                    } else {
                        // 避免菜单未设置权限，就默认超级管理员能用！防止匿名用户访问到还未设置权限的菜单！！！
                        set.add(new SecurityConfig(CommonConst.ROLE_APP_SUPER_ADMIN));
                    }
                }
            }
        }

        if (CollectionUtil.isNotEmpty(set)) {
            log.debug("本次请求需要拥有角色【{}】才可访问", set);
        } else {
            log.debug("本次请求无需任何角色即可访问");
        }
        return set;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }

    /**
     * 判断请求路径是否需要鉴权
     * @param contextPath   上下文路径
     * @param requestUrl    真实接口地址
     * @return  true：不需要鉴权；false：需要鉴权
     */
    private boolean isIgnoreUrl(String contextPath, String requestUrl) {
        // mvc 错误接口
        if (Objects.equals(contextPath, "/error") || Objects.equals(requestUrl, "/error")) {
            return true;
        }

        // 本次请求是静态资源，不需要进行后面的token校验
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean staticUri = CommonConst.STATIC_URIS.stream()
                .anyMatch(f -> antPathMatcher.match(f, requestUrl));
        if (staticUri) {
            return true;
        }

        // 本次请求时白名单，直接放行
        Optional<String> first = CommonConst.IGNORE_URIS.stream()
                .filter(f -> antPathMatcher.match(f, requestUrl))
                .findFirst();
        return first.isPresent();
    }
}
