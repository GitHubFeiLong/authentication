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
 *
 * @author chenf
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseUserCreateTokenReq extends BaseApiReq implements Serializable {
    //~fields
    //==================================================================================================================
    private static final long serialVersionUID = -828353412585164936L;
    /**
     * 用户名
     */
    private String username;
    //~methods
    //==================================================================================================================
}
