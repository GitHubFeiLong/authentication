package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 类描述：
 * 分页查询用户的参数对象
 * @author cfl
 * @version 1.0
 */
@Data
public class BaseUserPageReq extends BasePage {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "用户id集合", hidden = true)
    private List<Long> ids;

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户过期时间")
    private Date startValidTime;

    @ApiModelProperty("用户过期时间")
    private Date endValidTime;
}
