package com.goudong.authentication.server.util;

import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.core.ClientException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 类描述：
 * 获取用户工具类
 * @author chenf
 */
public class SecurityContextUtil {

    /**
     * 获取用户
     * @return 用户
     */
    public static MyAuthentication get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof MyAuthentication) {
            return (MyAuthentication) authentication;
        }
        throw ClientException.clientByUnauthorized();
    }
}
