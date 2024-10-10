package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 类描述：
 * 角色下拉分页
 * @author cfl
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class BaseRoleDropDownReq extends BasePage{
    //~fields
    //==================================================================================================================
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("角色名")
    private String name;
}
