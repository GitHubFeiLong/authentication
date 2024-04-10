package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：
 * 导出字典类型
 * @author chenf
 */
@Data
public class BaseDictTypeExportReq {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "勾选的id")
    private List<Long> ids;

    @ApiModelProperty(value = "分页查询参数")
    private BaseDictTypePageReq pageReq;
    //~methods
    //==================================================================================================================
}
