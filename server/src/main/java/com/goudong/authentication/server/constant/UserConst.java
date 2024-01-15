package com.goudong.authentication.server.constant;

/**
 * 类描述：
 *
 * @ClassName UserConst
 * @Author Administrator
 * @Date 2023/7/30 10:57
 * @Version 1.0
 */
public class UserConst {
    //~fields
    //==================================================================================================================
    @Deprecated
    public static final String ADMIN = "admin";

    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 访问token的有效时长，单位天
     */
    public static final Integer JWT_ACCESS_EXPIRATION_DAYS = 1;


    //~methods
    //==================================================================================================================
}
