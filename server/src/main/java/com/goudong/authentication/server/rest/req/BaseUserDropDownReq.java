package com.goudong.authentication.server.rest.req;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class BaseUserDropDownReq extends BasePage{
    //~fields
    //==================================================================================================================
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty(value = "应用名", hidden = true)
    @JsonIgnore
    private Long realAppId;
}
