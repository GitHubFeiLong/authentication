package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：
 * 菜单分页查询
 * @author cfl
 * @version 1.0
 */
@Data
public class BaseMenuGetAllReq {
    //~fields
    //==================================================================================================================

    @ApiModelProperty(value = "菜单id集合", hidden = true)
    private List<Long> ids;

    @ApiModelProperty(value = "应用Id", hidden = true)
    private Long appId;

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("菜单类型")
    private Integer type;

    @ApiModelProperty("权限标识")
    private String permissionId;

    @ApiModelProperty("资源路径")
    private String path;


}
