package com.goudong.authentication.server.rest.req;

import com.goudong.authentication.common.util.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    @NotBlank(message="[字典配置名称]不能为空")
    @Size(max= 16,message="字典配置名称长度不能超过16")
    @ApiModelProperty("字典配置名称")
    private String name;

    @ApiModelProperty(value = "配置模板注释", required = true)
    private String template;

    @ApiModelProperty(value = "字典JSON配置", required = true)
    private String setting;

    /**
     * 是否激活（true：已激活；false：未激活）
     */
    @ApiModelProperty("是否激活（true：已激活；false：未激活）")
    private Boolean enabled;

    /**
     * 备注
     */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("备注")
    private String remark;
    //~methods
    //==================================================================================================================

    /**
     * 属性设置默认值
     */
    public void attributeDefaultValue() {
        // 参数校验，设置默认值
        if (StringUtil.isBlank(this.getTemplate())) {
            this.template = "{}";
        }
        if (StringUtil.isBlank(this.getSetting())) {
            this.setting = "{}";
        }
        if (this.getEnabled() == null) {
            this.enabled = false;
        }
    }
}
