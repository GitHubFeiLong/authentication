package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 类描述：
 * 修改应用
 * @author cfl
 * @version 1.0
 * @date 2023/7/21 11:13
 */
@Data
public class BaseAppUpdate {

    @NotNull
    @ApiModelProperty(value = "应用id", required = true)
    private Long id;

    /**
     * 登录回调页面，处理登录逻辑
     */
    @Size(max = 255)
    @ApiModelProperty(value = "登录回调页面，处理登录逻辑")
    private String homePage;

    /**
     * 是否激活应用
     */
    @ApiModelProperty(value = "是否激活应用")
    private Boolean enabled;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
