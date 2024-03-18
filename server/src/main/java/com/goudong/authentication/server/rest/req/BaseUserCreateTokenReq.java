package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 类描述：
 *
 * @author chenf
 */
@Data
public class BaseUserCreateTokenReq {
    //~fields
    //==================================================================================================================
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank
    private String username;
    //~methods
    //==================================================================================================================
}
