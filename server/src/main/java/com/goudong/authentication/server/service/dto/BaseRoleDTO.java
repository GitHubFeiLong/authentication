package com.goudong.authentication.server.service.dto;

import com.goudong.authentication.server.domain.BaseRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link BaseRole} entity.
 */
@ApiModel(description = "角色表")
@Data
public class BaseRoleDTO implements Serializable {

    private static final long serialVersionUID = 7000635489217514230L;

    private Long id;

    /**
     * 应用id
     */
    @NotNull
    @ApiModelProperty(value = "应用id", required = true)
    private Long appId;

    /**
     * 角色名称
     */
    @NotNull
    @Size(min = 4, max = 16)
    @ApiModelProperty(value = "角色名称", required = true)
    private String name;

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
