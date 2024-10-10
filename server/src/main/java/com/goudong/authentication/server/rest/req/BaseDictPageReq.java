package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 类描述：
 * 分页字典
 * @author chenf
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseDictPageReq extends BasePage {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "字典id集合", hidden = true)
    private List<Long> ids;

    @ApiModelProperty(value = "字典类型主键", required = true)
    private Long dictTypeId;

    @ApiModelProperty("字典编码")
    private String code;

    @ApiModelProperty("字典名称")
    private String name;

    //~methods
    //==================================================================================================================
}
