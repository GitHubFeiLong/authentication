package com.goudong.authentication.server.rest.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：
 * 字典配置分页结果
 * @author chenf
 */
@Data
public class BaseDictSettingPageResp {
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
     * 字典类型主键
     */
    @ApiModelProperty(value = "字典类型主键")
    private Long dictTypeId;
    /**
     * 字典主键
     */
    @ApiModelProperty(value = "字典主键")
    private Long dictId;
    /**
     * 字典配置名称
     */
    @ApiModelProperty(value = "配置名称")
    private String name;
    /**
     * 配置模板注释
     */
    @ApiModelProperty(value = "配置模板注释")
    private String template;
    /**
     * 字典JSON配置
     */
    @ApiModelProperty(value = "字典JSON配置")
    private String setting;
    /**
     * 是否激活（true：已激活；false：未激活）
     */
    @ApiModelProperty(value = "是否激活（true：已激活；false：未激活）")
    private Boolean enabled;
    /**
     * 是否是默认的（true：默认的；false：非默认的）
     */
    @ApiModelProperty(value = "是否是默认的（true：默认的；false：非默认的）")
    private Boolean defaulted;
    /**
     * 说明备注
     */
    @ApiModelProperty(value = "说明备注")
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
