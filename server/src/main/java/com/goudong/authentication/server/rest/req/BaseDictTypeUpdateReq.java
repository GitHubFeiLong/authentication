package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 类描述：
 * 修改字典类型
 * @author chenf
 */
@Data
public class BaseDictTypeUpdateReq {
    //~fields
    //==================================================================================================================
    /**
     * 字典类型ID
     */
    @NotNull(message="[字典类型ID]不能为空")
    @ApiModelProperty(value = "字典类型ID", required = true)
    private Long id;

    /**
     * 字典类型编码
     */
    @NotBlank(message="[字典类型编码]不能为空")
    @Size(max= 16,message="编码长度不能超过16")
    @ApiModelProperty(value = "字典类型编码", required = true)
    @Length(max= 16,message="编码长度不能超过16")
    private String code;
    /**
     * 字典类型名称
     */
    @NotBlank(message="[字典类型名称]不能为空")
    @Size(max= 16,message="编码长度不能超过16")
    @ApiModelProperty("字典类型名称")
    @Length(max= 16,message="编码长度不能超过16")
    private String name;

    /**
     * 字典配置模板注释
     */
    @ApiModelProperty("字典配置模板注释")
    @NotBlank(message="[字典配置模板注释]不能为空")
    @Column(columnDefinition = "json" )
    private String template;

    /**
     * 是否激活（true：已激活；false：未激活）
     */
    @NotNull(message="[是否激活（true：已激活；false：未激活）]不能为空")
    @ApiModelProperty("是否激活（true：已激活；false：未激活）")
    private Boolean enabled;
    /**
     * 备注
     */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("备注")
    @Length(max= 255,message="编码长度不能超过255")
    private String remark;
    //~methods
    //==================================================================================================================
}
