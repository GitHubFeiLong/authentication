package com.goudong.authentication.server.config.security;

import com.goudong.authentication.common.constant.CommonConst;
import com.goudong.authentication.common.core.Jwt;
import com.goudong.authentication.common.core.UserSimple;
import com.goudong.authentication.common.util.HttpRequestUtil;
import com.goudong.authentication.server.constant.HttpHeaderConst;
import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.domain.BaseRole;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.pojo.GoudongSHA256withRSARequestHeaderParameter;
import com.goudong.authentication.server.service.dto.BaseAppCertDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseAppManagerService;
import com.goudong.authentication.server.util.SecurityContextUtil;
import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.core.security.cer.CertificateUtil;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.ListUtil;
import com.goudong.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 自定义填充 SecurityContextHolder
 * @author chenf
 */
@Component
@Slf4j
public class MySecurityContextPersistenceFilter extends OncePerRequestFilter {
    //~fields
    //==================================================================================================================

    /**
     * 应用管理服务接口
     */
    @Resource
    private BaseAppManagerService baseAppManagerService;

    //~methods
    //==================================================================================================================

    /**
     * 将用户解析保存到上下文中，程序中使用{@link SecurityContextUtil#get()}
     * @param httpServletRequest    请求对象
     * @param httpServletResponse   响应对象
     * @param filterChain           过滤器链
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 获取上下文路径
            // 获取请求地址，不包含上下文（server.servlet.context-path），这样方便迁移。
            String requestUrl = httpServletRequest.getRequestURI();

            // 本次请求是静态资源，不需要进行后面的token校验
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            boolean staticUri = com.goudong.authentication.server.constant.CommonConst.STATIC_URIS.stream()
                    .anyMatch(f -> antPathMatcher.match(f, requestUrl));
            if (staticUri) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            String authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
            // 未携带令牌，就直接放行
            if (StringUtil.isBlank(authorization)) {
                // 填充上下文用户，使用匿名用户
                MyAuthentication myAuthentication = new MyAuthentication();
                myAuthentication.setId(-1L);
                Long appId = getAppId(httpServletRequest);
                // 设置应用id，后续请求鉴权时需要根据应用查询是否需要权限。
                myAuthentication.setAppId(appId);
                myAuthentication.setRealAppId(appId);
                myAuthentication.setUsername(CommonConst.USER_ANONYMOUS);
                myAuthentication.setRoles(ListUtil.newArrayList(new SimpleGrantedAuthority(CommonConst.ROLE_ANONYMOUS)));
                myAuthentication.setAuthenticated(false);
                // 官网建议，避免跨多个线程的竞态条件
                SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
                emptyContext.setAuthentication(myAuthentication);
                SecurityContextHolder.setContext(emptyContext);
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
            // 携带了令牌，需要将其解析成用户
            AssertUtil.isNotBlank(authorization, () -> ClientException.clientByUnauthorized());
            Pattern pattern = Pattern.compile("(Bearer || GOUDONG-SHA256withRSA ).*");
            Matcher matcher = pattern.matcher(authorization);
            AssertUtil.isTrue(matcher.matches(), "请求头Authorization格式错误");
            String model = matcher.group(1);
            UserSimple userSimple;
            if (model.equals(CommonConst.TOKEN_MODEL_BEARER)) { // 直接解析token
                Long appId = getAppId(httpServletRequest);
                // 设置应用id到请求属性中，供后续使用
                httpServletRequest.setAttribute(HttpHeaderConst.X_APP_ID, appId);
                BaseApp app = baseAppManagerService.findById(appId);
                AssertUtil.isTrue(app.getEnabled(), () -> ClientException
                        .builder()
                        .exceptionEnum(ClientExceptionEnum.UNAUTHORIZED)
                        .clientMessage("当前应用已冻结，用户无法访问")
                        .serverMessageTemplate("X-App-Id:{} 未激活")
                        .serverMessageParams(appId)
                        .build());
                String token = authorization.substring(7);
                userSimple = Jwt.parseToken(app.getSecret(), token);

                log.debug("解析token：{}", userSimple);
            } else {
                userSimple = getAppAdminUser(authorization);
            }

            // 获取认证用户，并将其设置到 SecurityContext中
            MyAuthentication myAuthentication = new MyAuthentication();
            myAuthentication.setId(userSimple.getId());
            myAuthentication.setAppId(userSimple.getAppId());
            myAuthentication.setRealAppId(userSimple.getRealAppId());
            myAuthentication.setUsername(userSimple.getUsername());
            myAuthentication.setRoles(userSimple.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
            // 官网建议，避免跨多个线程的竞态条件
            SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
            emptyContext.setAuthentication(myAuthentication);
            SecurityContextHolder.setContext(emptyContext);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    /**
     * 获取请求头的appId
     * @param httpServletRequest
     * @return
     */
    public Long getAppId(HttpServletRequest httpServletRequest) {
        // 先校验请求头应用Id
        String appIdStr = httpServletRequest.getHeader(HttpHeaderConst.X_APP_ID);
        AssertUtil.isNotBlank(appIdStr, () -> BasicException.client(String.format("请求头%s丢失", HttpHeaderConst.X_APP_ID)));
        try {
            return Long.parseLong(appIdStr);
        } catch (NumberFormatException e) {
            throw BasicException.client(String.format("请求头%s=%s无效", HttpHeaderConst.X_APP_ID, appIdStr));
        }
    }

    /**
     * 获取应用管理员账户
     * @param authentication    请求头令牌
     * @return  应用的管理员信息
     */
    public UserSimple getAppAdminUser(String authentication) {
        GoudongSHA256withRSARequestHeaderParameter parameter = GoudongSHA256withRSARequestHeaderParameter.getInstance(authentication);
        // 提取关键信息
        Long appId = parameter.getAppId();                  // 应用id
        String serialNumber = parameter.getSerialNumber();  // 16位证书序列号

        // 查询应用证书
        BaseApp baseApp = baseAppManagerService.findById(appId);
        AssertUtil.isNotNull(baseApp, () -> BasicException.client(String.format("应用id=%s不存在", appId)));
        // 查询证书信息
        BaseAppCertDTO baseAppCertDTO = baseAppManagerService.getCertBySerialNumber(serialNumber);
        AssertUtil.isNotNull(baseAppCertDTO, () -> BasicException.client("证书序列号无效"));
        AssertUtil.isTrue(Objects.equals(baseAppCertDTO.getAppId(), appId), () -> BasicException.client("证书无效").serverMessage("应用和证书不匹配"));
        AssertUtil.isTrue(baseAppCertDTO.getValidTime().after(new Date()), () -> BasicException.client("证书无效").serverMessage("证书已过期"));

        // 查询应用管理员
        BaseUser baseUser = baseAppManagerService.findAppAdminUser(baseApp.getId(), baseApp.getName());
        AssertUtil.isNotNull(baseUser, () -> BasicException.client(String.format("应用%s管理员不存在", baseApp.getName())));

        // 构造实体
        UserSimple userSimple = new UserSimple();
        userSimple.setId(baseUser.getId());
        userSimple.setAppId(baseUser.getAppId());
        userSimple.setRealAppId(baseUser.getRealAppId());
        userSimple.setUsername(baseUser.getUsername());
        userSimple.setRoles(baseUser.getRoles().stream().map(BaseRole::getName).collect(Collectors.toList()));
        return userSimple;
    }

}
