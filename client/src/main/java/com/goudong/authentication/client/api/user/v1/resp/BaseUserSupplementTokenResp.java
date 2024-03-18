package com.goudong.authentication.client.api.user.v1.resp;

import lombok.Data;

import java.util.Date;

/**
 * 类描述：
 * 补充token响应的新token信息
 * @author chenf
 */
@Data
public class BaseUserSupplementTokenResp {
    //~fields
    //==================================================================================================================
    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * accessToken失效时长
     */
    private Date accessExpires;
    /**
     * refreshToken失效时长
     */
    private Date refreshExpires;

    //~methods
    //==================================================================================================================
}
