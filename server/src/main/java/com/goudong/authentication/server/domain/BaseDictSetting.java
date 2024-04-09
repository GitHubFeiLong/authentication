package com.goudong.authentication.server.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

/**
* 字典配置，针对字典进行配置详细信息
* @TableName base_dict_setting
*/
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "base_dict_setting")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
public class BaseDictSetting extends BasePO implements Serializable {

    private static final long serialVersionUID = -8861564686356414095L;

    /**
    * 应用id
    */
    @NotNull(message="[应用id]不能为空")
    @ApiModelProperty(value = "应用id", required = true)
    private Long appId;
    /**
     * 字典类型主键
     */
    @NotNull(message="[字典类型主键]不能为空")
    @ApiModelProperty(value = "字典类型主键", required = true)
    private Long dictTypeId;

    /**
    * 字典主键
    */
    @NotNull(message="[字典主键]不能为空")
    @ApiModelProperty(value = "字典主键", required = true)
    @Column(name = "dict_id",insertable = false, updatable = false)
    private Long dictId;
    /**
     * 字典配置名称
     */
    @NotBlank(message="[配置名称]不能为空")
    @ApiModelProperty(value = "配置名称", required = true)
    @Size(max= 16,message="编码长度不能超过16")
    @Length(max= 16,message="编码长度不能超过16")
    private String name;
    /**
    * 配置模板注释
    */
    @NotNull(message="[配置模板注释]不能为空")
    @ApiModelProperty(value = "配置模板注释", required = true)
    @Column(columnDefinition = "json" )
    private String template;
    /**
    * 字典JSON配置
    */
    @NotNull(message="[字典JSON配置]不能为空")
    @ApiModelProperty(value = "字典JSON配置", required = true)
    @Column(columnDefinition = "json" )
    private String setting;
    /**
    * 是否激活（true：已激活；false：未激活）
    */
    @NotNull(message="[是否激活（true：已激活；false：未激活）]不能为空")
    @ApiModelProperty(value = "是否激活（true：已激活；false：未激活）", required = true)
    private Boolean enabled;
    /**
    * 是否是默认的（true：默认的；false：非默认的）
    */
    @NotNull(message="[是否是默认的（true：默认的；false：非默认的）]不能为空")
    @ApiModelProperty(value = "是否是默认的（true：默认的；false：非默认的）", required = true)
    private Boolean defaulted;
    /**
    * 备注
    */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("备注")
    @Length(max= 255,message="编码长度不能超过255")
    private String remark;
}
