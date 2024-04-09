package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * 修改字典类型激活状态请求对象
 * @author chenf
 */
@Data
public class BaseDictTypeChangeEnabledReq {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "字典类型ID", required = true)
    private Long id;
    //~methods
    //==================================================================================================================
}
