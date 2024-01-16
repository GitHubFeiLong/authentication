package com.goudong.authentication.server.config.security;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：
 * 认证时详细信息配置（除了用户名和密码，还可以自定义配置额外属性）
 * @see WebAuthenticationDetailsImpl
 * @author chenf
 */
@Component
public class AuthenticationDetailsSourceImpl implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    /**
     * 将认证参数构建完善
     * @param httpServletRequest    请求对象，身份验证详细信息可能使用该对象
     * @return web认证详细参数信息
     */
    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest httpServletRequest) {
        return new WebAuthenticationDetailsImpl(httpServletRequest);
    }
}
