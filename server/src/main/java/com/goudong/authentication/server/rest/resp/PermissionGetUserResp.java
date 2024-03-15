package com.goudong.authentication.server.rest.resp;

import com.goudong.authentication.server.service.dto.BaseRoleDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 类描述：
 * 获取用户接口响应对象
 * @author chenf
 */
@Data
public class PermissionGetUserResp {
    //~fields
    //==================================================================================================================
    /**
     * 应用id
     */
    @ApiModelProperty(value = "应用id", required = true)
    private Long appId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    private String username;


    /**
     * 激活状态：true 激活；false 未激活
     */
    @ApiModelProperty(value = "激活状态：true 激活；false 未激活", required = true)
    private Boolean enabled;

    /**
     * 锁定状态：true 锁定；false 未锁定
     */
    @ApiModelProperty(value = "锁定状态：true 锁定；false 未锁定", required = true)
    private Boolean locked;

    /**
     * 有效截止时间
     */
    @ApiModelProperty(value = "有效截止时间", required = true)
    private Date validTime;

    /**
     * 角色
     */
    @ApiModelProperty(value = "角色", required = true)
    private List<BaseRoleDTO> roles;
    //~methods
    //==================================================================================================================
}
