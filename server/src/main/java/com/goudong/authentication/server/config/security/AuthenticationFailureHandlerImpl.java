package com.goudong.authentication.server.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.authentication.server.bean.ApiLogBean;
import com.goudong.authentication.server.exception.BasicException;
import com.goudong.authentication.server.exception.ClientExceptionEnum;
import com.goudong.authentication.server.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * 认证失败处理器
 * @author chenf
 */
@Slf4j
@SuppressWarnings("Duplicates")
@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    //~fields
    //==================================================================================================================
    /**
     * 日志打印
     */
    @Resource
    private ApiLogBean apiLogBean;

    //~methods
    //==================================================================================================================

    /**
     * 当认证过程失败时
     * @param httpServletRequest    请求对象
     * @param httpServletResponse   响应对象
     * @param e                     认证过程中发生的异常
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        ClientExceptionEnum exceptionEnum = ClientExceptionEnum.UNAUTHORIZED;
        Result<Object> result = Result.ofFail(exceptionEnum);
        log.warn("自定义登录失败处理器:{}", e.getMessage());
        // TODO是否需要返回200
        if (e instanceof UsernameNotFoundException) {
            // 为了更加安全，提示用户名或密码错误（小技巧）
            result.setClientMessage("用户名或密码错误");
        } else if (e instanceof BadCredentialsException) {
            // 为了更加安全，提示用户名或密码错误（小技巧）
            result.setClientMessage("用户名或密码错误");
        } else if (e instanceof AccountExpiredException) {
            result.setClientMessage(e.getMessage());
        } else {
            result.setClientMessage(e.getMessage());
        }

        httpServletResponse.setStatus(exceptionEnum.getStatus());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        String json = new ObjectMapper().writeValueAsString(result);
        apiLogBean.print(result);
        httpServletResponse.getWriter().write(json);
    }
}
