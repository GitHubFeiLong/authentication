package com.goudong.authentication.server.rest.req;

import com.goudong.authentication.server.rest.req.search.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * 字典类型下拉参数
 * @author chenf
 */
@Data
public class BaseDictTypeDropDownResp {
    //~fields
    //==================================================================================================================
    /**
     * 字典类型ID
     */
    @ApiModelProperty(value = "字典类型ID", required = true)
    private Long id;

    @ApiModelProperty(value = "字典类型编码", required = true)
    private String code;

    @ApiModelProperty(value = "字典类型名称", required = true)
    private String name;

    //~methods
    //==================================================================================================================

    public BaseDictTypeDropDownResp() {
    }

    public BaseDictTypeDropDownResp(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}
