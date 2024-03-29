package com.goudong.authentication.server.rest.req;

import com.goudong.authentication.server.rest.req.search.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 类描述：
 * 分页字典类型
 * @author chenf
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseDictTypePageReq extends BasePage {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "字典类型id集合", hidden = true)
    private List<Long> ids;

    @ApiModelProperty("字典类型编码")
    private String code;

    @ApiModelProperty("字典类型名称")
    private String name;

    //~methods
    //==================================================================================================================
}
