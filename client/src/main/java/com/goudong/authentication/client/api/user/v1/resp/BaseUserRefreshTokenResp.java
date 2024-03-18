package com.goudong.authentication.client.api.user.v1.resp;

import lombok.Data;

/**
 * 类描述：
 * 刷新token响应对象
 * @author chenf
 */
@Data
public class BaseUserRefreshTokenResp {
    //~fields
    //==================================================================================================================
    /**
     * 刷新token
     */
    private String refreshToken;
    //~methods
    //==================================================================================================================
}
