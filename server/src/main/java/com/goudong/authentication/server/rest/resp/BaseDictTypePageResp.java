package com.goudong.authentication.server.rest.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：
 * 字典类型分页结果
 * @author chenf
 */
@Data
public class BaseDictTypePageResp {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "序号")
    private Long serialNumber;

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
     * 字典类型编码
     */
    @ApiModelProperty(value = "字典类型编码")
    private String code;
    /**
     * 	字典类型名称
     */
    @ApiModelProperty(value = "字典类型名称")
    private String name;
    /**
     * 字典配置模板注释
     */
    @ApiModelProperty(value = "字典配置模板注释")
    private String template;
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

    /**
     * 字典类型下的数量
     */
    @ApiModelProperty(value = "字典数量")
    private Integer dictNumber;

    //~methods
    //==================================================================================================================
}
