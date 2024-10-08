package com.goudong.authentication.server.config.security;

import com.goudong.authentication.server.constant.DateConst;
import com.goudong.authentication.server.exception.BasicException;
import com.goudong.authentication.server.exception.ClientException;
import com.goudong.authentication.server.exception.ClientExceptionEnum;
import com.goudong.authentication.server.lang.Result;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * 未经身份验证
 * @author chenf
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        ClientExceptionEnum notAuthentication = ClientExceptionEnum.UNAUTHORIZED;
        Result<Object> result = Result.ofFail(new ClientException(notAuthentication));

        httpServletResponse.setStatus(notAuthentication.getStatus());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setHeader(HttpHeaders.WWW_AUTHENTICATE, HttpHeaders.WWW_AUTHENTICATE);
        String json = new Jackson2ObjectMapperBuilder()
                .simpleDateFormat(DateConst.DATE_TIME_FORMATTER)
                .build()
                .writeValueAsString(Result.ofSuccess(result));;
        httpServletResponse.getWriter().write(json);
    }
}
