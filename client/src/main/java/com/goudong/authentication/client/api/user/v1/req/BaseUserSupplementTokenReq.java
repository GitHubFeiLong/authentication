package com.goudong.authentication.client.api.user.v1.req;

import com.goudong.authentication.client.core.BaseApiReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * 类描述：
 * 补充token信息
 * @author chenf
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseUserSupplementTokenReq extends BaseApiReq {
    //~fields
    //==================================================================================================================
    /**
     * 【必填】用户令牌
     */
    private String token;

    /**
     * 【必填】待添加的参数
     */
    private Map<String, Object> detail;

    //~methods
    //==================================================================================================================
}
