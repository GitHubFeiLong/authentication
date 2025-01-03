package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 类描述：
 * 新增应用
 * @author cfl
 * @version 1.0
 * @date 2023/7/21 11:13
 */
@Data
public class BaseAppCreate {
    /**
     * 应用名称
     */
    @NotBlank(message = "name不能为空")
    @Size(max = 16)
    @ApiModelProperty(value = "应用名称", required = true)
    private String name;

    /**
     * 登录回调页面，处理登录逻辑
     */
    @Size(max = 255, message = "登录回调页面，处理登录逻辑地址不能超过255")
    @NotBlank
    @ApiModelProperty(value = "登录回调页面，处理登录逻辑")
    private String homePage;

    /**
     * 是否激活应用
     */
    @NotNull
    @ApiModelProperty(value = "是否激活应用", required = true)
    private Boolean enabled = true;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
