package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * 字典明细下拉响应
 * @author chenf
 */
@Data
public class BaseDictDropDownResp {
    //~fields
    //==================================================================================================================
    /**
     * 字典类型ID
     */
    @ApiModelProperty(value = "字典明细ID", required = true)
    private Long id;

    @ApiModelProperty(value = "字典明细编码", required = true)
    private String code;

    @ApiModelProperty(value = "字典明细名称", required = true)
    private String name;

    //~methods
    //==================================================================================================================

    public BaseDictDropDownResp() {
    }

    public BaseDictDropDownResp(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}
