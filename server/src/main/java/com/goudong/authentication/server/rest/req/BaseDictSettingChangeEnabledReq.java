package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * 修改字典配置激活状态请求对象
 * @author chenf
 */
@Data
public class BaseDictSettingChangeEnabledReq {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "字典ID", required = true)
    private Long id;
    //~methods
    //==================================================================================================================
}
