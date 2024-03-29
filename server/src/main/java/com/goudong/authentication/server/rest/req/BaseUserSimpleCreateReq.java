package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 新增用户
 * @author chenf
 */
@Data
public class BaseUserSimpleCreateReq implements Serializable {


    private static final long serialVersionUID = -7466849523525350832L;
    /**
     * 用户名
     */
    @NotNull
    @Size(max = 16)
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    /**
     * 密码{@code password == null || password.length() == 0} 时，使用系统设置的默认密码
     */
    @Size(max = 32)
    @ApiModelProperty(value = "密码，如果参数是空串，就使用默认密码", required = false)
    private String password;

    /**
     * 密码
     */
    @NotNull
    @NotEmpty
    @ApiModelProperty(value = "角色id", required = true)
    private List<Long> roleIds;

    /**
     * 备注
     */
    @Size(max = 255)
    @ApiModelProperty(value = "备注")
    private String remark;
}
