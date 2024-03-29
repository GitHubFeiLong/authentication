package com.goudong.authentication.server.service.dto;

import com.goudong.authentication.server.domain.BaseDict;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：
 * A DTO for the {@link BaseDict} entity.
 * @author chenf
 */
@Data
public class BaseDictDTO {
    //~fields
    //==================================================================================================================
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;
    /**
     * 应用id
     */
    @ApiModelProperty(value = "应用id")
    private Long appId;
    /**
     * 字典类型主键
     */
    @ApiModelProperty(value = "字典类型主键")
    private Long dictTypeId;
    /**
     * 字典编码,同一应用下编码唯一
     */
    @ApiModelProperty(value = "字典编码,同一应用下编码唯一")
    private String code;
    /**
     * 字典名
     */
    @ApiModelProperty(value = "字典名")
    private String name;
    /**
     * 是否激活（true：已激活；false：未激活）
     */
    @ApiModelProperty(value = "是否激活（true：已激活；false：未激活）")
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


    //~methods
    //==================================================================================================================
}
