package com.goudong.authentication.server.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

/**
* 字典明细，对字典类型进行细分
* @TableName base_dict
*/
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "base_dict")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
public class BaseDict extends BasePO implements Serializable {

    private static final long serialVersionUID = -7595742289723037458L;

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
    * 字典编码,同一应用下编码唯一
    */
    @NotBlank(message="[字典类型主键]不能为空")
    @Size(max= 16,message="编码长度不能超过16")
    @ApiModelProperty(value = "字典编码,同一应用下编码唯一", required = true)
    @Length(max= 16,message="编码长度不能超过16")
    private String code;
    /**
    * 字典名
    */
    @NotBlank(message="[字典名]不能为空")
    @Size(max= 16,message="编码长度不能超过16")
    @ApiModelProperty(value = "字典名", required = true)
    @Length(max= 16,message="编码长度不能超过16")
    private String name;
    /**
    * 是否激活（true：已激活；false：未激活）
    */
    @NotNull(message="[是否激活（true：已激活；false：未激活）]不能为空")
    @ApiModelProperty(value = "是否激活（true：已激活；false：未激活）",required = true)
    private Boolean enabled;
    /**
    * 备注
    */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("备注")
    @Length(max= 255,message="编码长度不能超过255")
    private String remark;
}
