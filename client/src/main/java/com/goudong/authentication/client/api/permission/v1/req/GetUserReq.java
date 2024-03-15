package com.goudong.authentication.client.api.permission.v1.req;

import com.goudong.authentication.client.core.BaseApiReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 类描述：
 * 获取用户信息
 * @author chenf
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class GetUserReq extends BaseApiReq {
    //~fields
    //==================================================================================================================
    /**
     * 用户名
     */
    private String username;

    //~methods
    //==================================================================================================================
}
