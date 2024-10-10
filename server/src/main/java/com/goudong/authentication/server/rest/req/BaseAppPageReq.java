package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 类描述：
 * 应用分页查询请求参数
 * @author cfl
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseAppPageReq extends BasePage {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("应用id集合")
    private List<Long> ids;

    @ApiModelProperty("应用id")
    private Long id;

    @ApiModelProperty("应用名称")
    private String name;

    @ApiModelProperty("首页地址")
    private String homePage;

    @ApiModelProperty("激活状态")
    private Boolean enabled;

    @ApiModelProperty("备注")
    private String remark;

    //~methods
    //==================================================================================================================
}
