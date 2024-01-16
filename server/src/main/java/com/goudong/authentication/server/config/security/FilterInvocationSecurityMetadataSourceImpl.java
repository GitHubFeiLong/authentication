package com.goudong.authentication.server.config.security;

import com.goudong.authentication.common.constant.CommonConst;
import com.goudong.authentication.server.service.dto.ApiPermissionDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseMenuManagerService;
import com.goudong.authentication.server.util.SecurityContextUtil;
import com.goudong.core.util.CollectionUtil;
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
        // mvc 错误接口
        if (Objects.equals(contextPath, "/error")) {
            return null;
        }

        // 获取请求地址，不包含上下文（server.servlet.context-path），这样方便迁移。
        String requestUrl = ((FilterInvocation) o).getRequestUrl();

        // 资源需要的权限集合，如果是空集合就不需要校验权限。
        Set<ConfigAttribute> set = new HashSet<>();

        // 获取请求的方法
        String requestMethod = ((FilterInvocation) o).getHttpRequest().getMethod().toUpperCase();
        log.debug("requestUrl >> {}，requestMethod >> {}", requestUrl, requestMethod);

        MyAuthentication myAuthentication = SecurityContextUtil.get();
        // 不是超级管理员，就需要校验权限
        if (!myAuthentication.superAdmin()) {
            // 查询指定应用，包含指定请求方式
            List<ApiPermissionDTO> apiPermissionDTOS = baseMenuManagerService.listApiPermissionByAppId(myAuthentication.getRealAppId());
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            for (ApiPermissionDTO permissionDTO : apiPermissionDTOS) {
                if (antPathMatcher.match(permissionDTO.getPath(), requestUrl) && permissionDTO.getMethod().contains(requestMethod)) {
                    // 如果菜单未配置角色，那么就默认只能是超级管理员才能访问
                    if (CollectionUtil.isEmpty(permissionDTO.getRoles())) {
                        set.add(new SecurityConfig(CommonConst.ROLE_APP_SUPER_ADMIN));
                    } else {
                        set.addAll(permissionDTO.getRoles().stream().map(SecurityConfig::new).collect(Collectors.toList()));
                    }
                }
            }
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
}
