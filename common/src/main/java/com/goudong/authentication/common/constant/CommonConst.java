package com.goudong.authentication.common.constant;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
public class CommonConst {
    //~fields
    //==================================================================================================================
    /**
     * jwt刷新token的有效时长是访问token时长的倍数
     */
    public static final Integer JWT_REFRESH_EXPIRATION_MULTIPLE = 2;

    /**
     * 请求头：X-App-Id
     */
    public static final String HTTP_HEADER_X_APP_ID = "X-App-Id";

    /**
     * 令牌模式
     */
    public static final String TOKEN_MODEL = "Bearer ";
    //~methods
    //==================================================================================================================
}