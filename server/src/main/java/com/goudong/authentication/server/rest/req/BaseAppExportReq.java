package com.goudong.authentication.server.rest.req;

import com.goudong.authentication.server.rest.req.search.BaseAppPageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：
 * 导出应用
 * @author chenf
 */
@Data
public class BaseAppExportReq {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "勾选的id")
    private List<Long> ids;

    @ApiModelProperty(value = "分页查询参数")
    private BaseAppPageReq pageReq;
    //~methods
    //==================================================================================================================
}
