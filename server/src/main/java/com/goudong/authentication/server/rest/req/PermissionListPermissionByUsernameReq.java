package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 类描述：
 * 获取用户权限列表的请求对象
 * @author chenf
 */
@Data
public class PermissionListPermissionByUsernameReq {
    //~fields
    //==================================================================================================================
    @NotBlank(message = "username不能为空")
    @ApiModelProperty(value = "用户名", required = true)
    private String username;
    //~methods
    //==================================================================================================================
}
