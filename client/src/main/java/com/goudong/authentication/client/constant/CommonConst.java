package com.goudong.authentication.client.constant;

import java.time.format.DateTimeFormatter;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 */
public class CommonConst {
    //~fields
    //==================================================================================================================

    /**
     * 超级管理员角色
     */
    public static final String ROLE_APP_SUPER_ADMIN = "ROLE_APP_SUPER_ADMIN";

    /**
     * 创建应用时，创建一个应用管理员在server端
     */
    public static final String ROLE_APP_ADMIN = "ROLE_APP_ADMIN";

    /**
     * 请求头属性
     */
    public static final String HTTP_HEADER_AUTHORIZATION = "Authorization";

    /**
     * 应用id请求头
     */
    public static final String HTTP_HEADER_X_APP_ID = "X-App-Id";

    /**
     * application/json;
     */
    public static final String HTTP_HEADER_VALUE_APPLICATION_JSON = "application/json";

    /**
     * 认证模式
     */
    public static final String AUTHENTICATION_TEMPLATE = "GOUDONG-SHA256withRSA appid=\"%s\",serial_number=\"%s\",timestamp=\"%s\",nonce_str=\"%s\",signature=\"%s\"";

    /**
     * 数字
     */
    public static final String DIGITAL = "0123456789";

    /**
     * 字母
     */
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 数字+字母
     */
    public static final String DIGITAL_ALPHABET = DIGITAL + ALPHABET;

    /**
     * 日期时间格式化
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    //~methods
    //==================================================================================================================
}
