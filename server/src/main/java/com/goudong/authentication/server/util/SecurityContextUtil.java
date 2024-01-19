package com.goudong.authentication.server.util;

import com.goudong.authentication.server.service.dto.MyAuthentication;
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
        return (MyAuthentication)SecurityContextHolder.getContext().getAuthentication();
    }
}
