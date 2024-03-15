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
public class PermissionGetUserReq {
    //~fields
    //==================================================================================================================
    /**
     * 用户名
     */
    @NotBlank
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    //~methods
    //==================================================================================================================
}
