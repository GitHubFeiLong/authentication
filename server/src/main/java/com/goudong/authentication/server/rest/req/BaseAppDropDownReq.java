package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * 应用下拉
 * @author cfl
 * @version 1.0
 */
@Data
public class BaseAppDropDownReq {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("应用id")
    private Long id;

    @ApiModelProperty("应用名")
    private String name;

}
