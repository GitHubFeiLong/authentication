package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 类描述：
 * 新增字典配置
 * @author chenf
 */
@Data
public class BaseDictSettingCreateReq {
    //~fields
    //==================================================================================================================
    /**
     * 字典类型编码
     */
    @NotNull(message="[字典主键]不能为空")
    @ApiModelProperty(value = "字典主键", required = true)
    private Long dictId;

    /**
     * 字典类型名称
     */
    @NotBlank(message="[字典类型名称]不能为空")
    @Size(max= 16,message="编码长度不能超过16")
    @ApiModelProperty("字典类型名称")
    @Length(max= 16,message="编码长度不能超过16")
    private String name;

    @ApiModelProperty(value = "配置模板注释", required = true)
    @NotBlank(message = "配置模板注释不能为空")
    private String template;

    @ApiModelProperty(value = "字典JSON配置", required = true)
    @NotBlank(message = "字典JSON配置不能为空")
    private String setting;

    /**
     * 是否激活（true：已激活；false：未激活）
     */
    @NotNull(message="[是否激活（true：已激活；false：未激活）]不能为空")
    @ApiModelProperty("是否激活（true：已激活；false：未激活）")
    private Boolean enabled = false;

    /**
     * 是否是默认的（true：默认的；false：非默认的）
     */
    @ApiModelProperty("是否是默认的（true：默认的；false：非默认的）")
    private Boolean defaulted = false;
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
