package com.goudong.authentication.server.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
* 字典类型，对字典进行分类管理
* @TableName base_dict_type
*/
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "base_dict_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
public class BaseDictType extends BasePO implements Serializable {

    private static final long serialVersionUID = 2346879709291848208L;

    /**
    * 应用id
    */
    @Column(name = "app_id")
    @NotNull(message="[应用id]不能为空")
    @ApiModelProperty(value = "应用id",required = true)
    private Long appId;
    /**
    * 字典类型编码
    */
    @Column(name = "code")
    @NotBlank(message="[字典类型编码]不能为空")
    @Size(max= 16,message="编码长度不能超过16")
    @ApiModelProperty(value = "字典类型编码", required = true)
    @Length(max= 16,message="编码长度不能超过16")
    private String code;
    /**
    * 字典类型名称
    */
    @Column(name = "name")
    @NotBlank(message="[字典类型名称]不能为空")
    @Size(max= 16,message="编码长度不能超过16")
    @ApiModelProperty(value = "字典类型名称",required = true)
    @Length(max= 16,message="编码长度不能超过16")
    private String name;

    /**
     * 字典配置模板注释
     */
    @Column(name = "template", columnDefinition = "json")
    @ApiModelProperty(value = "字典配置模板注释",required = true)
    @NotBlank(message="[字典配置模板注释]不能为空")
    private String template;

    /**
    * 是否激活（true：已激活；false：未激活）
    */
    @Column(name = "enabled")
    @NotNull(message="[是否激活（true：已激活；false：未激活）]不能为空")
    @ApiModelProperty(value = "是否激活（true：已激活；false：未激活）",required = true)
    private Boolean enabled;
    /**
    * 备注
    */
    @Column(name = "remark")
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("备注")
    @Length(max= 255,message="编码长度不能超过255")
    private String remark;
}
