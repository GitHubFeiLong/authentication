package com.goudong.authentication.common.constant;

import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * 全局通用常量
 * @author chenf
 * @version 1.0
 */
public class CommonConst {
    //~fields
    //==================================================================================================================
    /**
     * 请求头：X-App-Id
     */
    public static final String HTTP_HEADER_X_APP_ID = "X-App-Id";

    /**
     * 令牌模式
     */
    public static final String TOKEN_MODEL_BEARER = "Bearer ";

    /**
     * access token的有效时间，默认单位秒
     */
    public static final Long ACCESS_TOKEN_EXPIRATION = 3600L;

    /**
     * access token的有效时间单位，默认单位秒
     */
    public static final TimeUnit ACCESS_TOKEN_EXPIRATION_TIME_UNIT = TimeUnit.SECONDS;

    /**
     * refresh token的有效时间，默认单位秒
     */
    public static final Long REFRESH_TOKEN_EXPIRATION = 7200L;

    /**
     * refresh token的有效时间单位，默认单位秒
     */
    public static final TimeUnit REFRESH_TOKEN_EXPIRATION_TIME_UNIT = TimeUnit.SECONDS;

    /**
     * 超级管理员角色
     */
    public static final String ROLE_APP_SUPER_ADMIN = "ROLE_APP_SUPER_ADMIN";

    /**
     * 创建应用时，创建一个应用管理员在server端
     */
    public static final String ROLE_APP_ADMIN = "ROLE_APP_ADMIN";

    /**
     * 匿名角色
     * 注意，创建角色时不能再创建该同名角色。
     */
    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

    /**
     * 匿名用户
     */
    public static final String USER_ANONYMOUS = "USER_ANONYMOUS";


    //~methods
    //==================================================================================================================
}
