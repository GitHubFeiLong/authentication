package com.goudong.authentication.server.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.authentication.common.core.LoginResp;
import com.goudong.authentication.common.util.JsonUtil;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseUserManagerService;
import com.goudong.core.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * 认证成功处理器
 * @author chenf
 */
@Slf4j
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    //~fields
    //==================================================================================================================
    /**
     * 用户管理服务接口
     */
    @Resource
    private BaseUserManagerService baseUserManagerService;


    //~methods
    //==================================================================================================================
    /**
     * 认证成功处理
     * @param httpServletRequest    请求对象
     * @param httpServletResponse   响应对象
     * @param authentication        认证成功对象
     */
    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        httpServletResponse.setStatus(200);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");

        // 转换成自定义的Authentication对象
        MyAuthentication myAuthentication = (MyAuthentication) authentication;

        // 查询用户，角色，菜单
        LoginResp login = baseUserManagerService.login(myAuthentication);

        String json = JsonUtil.toJsonString(Result.ofSuccess(login));

        httpServletResponse.getWriter().write(json);
    }

}
