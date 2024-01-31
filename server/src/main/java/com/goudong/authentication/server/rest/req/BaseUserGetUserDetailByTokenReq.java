package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 类描述：
 * 根据token获取用户详细信息
 * @author chenf
 */
@Data
public class BaseUserGetUserDetailByTokenReq {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "token", required = true)
    @NotBlank(message = "token不能为空")
    private String token;

    //~methods
    //==================================================================================================================
}
