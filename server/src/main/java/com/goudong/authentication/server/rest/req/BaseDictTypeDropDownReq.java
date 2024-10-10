package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * 字典类型下拉参数
 * @author chenf
 */
@Data
public class BaseDictTypeDropDownReq extends BasePage {
    //~fields
    //==================================================================================================================
    /**
     * 字典类型编码
     */
    @ApiModelProperty("字典类型编码")
    private String code;

    //~methods
    //==================================================================================================================
}
