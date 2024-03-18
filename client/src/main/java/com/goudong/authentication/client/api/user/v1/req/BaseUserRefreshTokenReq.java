package com.goudong.authentication.client.api.user.v1.req;

import com.goudong.authentication.client.core.BaseApiReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 类描述：
 * 刷新token请求对象
 * @author chenf
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseUserRefreshTokenReq extends BaseApiReq implements Serializable {
    //~fields
    //==================================================================================================================
    private static final long serialVersionUID = -6744916728073929624L;
    /**
     * 刷新token
     */
    private String refreshToken;
    //~methods
    //==================================================================================================================
}
