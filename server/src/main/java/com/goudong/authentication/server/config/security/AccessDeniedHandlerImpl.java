package com.goudong.authentication.server.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.authentication.server.bean.ApiLogBean;
import com.goudong.authentication.server.exception.ClientExceptionEnum;
import com.goudong.authentication.server.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * 自定义权限不足处理器：返回状态码403
 * @author msi
 */
@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
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
     * 请求被拒绝处理方法
     * @param httpServletRequest    请求对象
     * @param httpServletResponse   响应对象
     * @param e                     访问拒绝异常对象
     */
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        ClientExceptionEnum notAuthorization = ClientExceptionEnum.FORBIDDEN;
        Result<?> result = Result.ofFail(notAuthorization);

        httpServletResponse.setStatus(notAuthorization.getStatus());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        String json = new ObjectMapper().writeValueAsString(result);

        apiLogBean.print(result);
        httpServletResponse.getWriter().write(json);
    }
}
