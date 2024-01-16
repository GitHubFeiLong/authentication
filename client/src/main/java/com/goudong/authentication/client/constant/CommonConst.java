package com.goudong.authentication.client.constant;

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
     * 认证模式
     */
    public static final String AUTHENTICATION_TEMPLATE = "GOUDONG-SHA256withRSA appid=\"%s\",serial_number=\"%s\",timestamp=\"%s\",nonce_str=\"%s\" signature=\"%s\"";

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
    //~methods
    //==================================================================================================================
}
