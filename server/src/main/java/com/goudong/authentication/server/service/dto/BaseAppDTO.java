package com.goudong.authentication.server.service.dto;

import com.goudong.authentication.server.domain.BaseApp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link BaseApp} entity.
 */
@ApiModel(description = "应用表")
@Data
public class BaseAppDTO implements Serializable {

    private Long id;

    /**
     * 应用密钥
     */
    @NotNull
    @Size(max = 64)
    @ApiModelProperty(value = "应用密钥", required = true)
    private String secret;

    /**
     * 应用名称
     */
    @NotNull
    @Size(max = 16)
    @ApiModelProperty(value = "应用名称", required = true)
    private String name;

    /**
     * 登录回调页面，处理登录逻辑
     */
    @NotNull
    @Size(max = 255)
    @ApiModelProperty(value = "登录回调页面，处理登录逻辑")
    private String homePage;

    /**
     * 是否激活
     */
    @NotNull
    @ApiModelProperty(value = "是否激活", required = true)
    private Boolean enabled;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createdDate;

    /**
     * 最后修改时间
     */
    @ApiModelProperty(value = "最后修改时间")
    private Date lastModifiedDate;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createdBy;

    /**
     * 最后修改人
     */
    @ApiModelProperty(value = "最后修改人")
    private String lastModifiedBy;
}
