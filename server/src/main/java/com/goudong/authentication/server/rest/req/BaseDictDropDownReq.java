package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * 字典明细下拉参数
 * @author chenf
 */
@Data
public class BaseDictDropDownReq extends BasePage {
    //~fields
    //==================================================================================================================

    /**
     * 字典类型ID
     */
    @ApiModelProperty("字典类型ID")
    private Long dictTypeId;

    /**
     * 字典明细编码
     */
    @ApiModelProperty("字典明细编码")
    private String code;

    //~methods
    //==================================================================================================================
}
