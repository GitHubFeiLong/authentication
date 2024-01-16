package com.goudong.authentication.server.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 类描述：
 * api权限实体
 * @author chenf
 */
@Data
public class ApiPermissionDTO {
    //~fields
    //==================================================================================================================
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 菜单名称
     */
    @NotNull
    @Size(min = 1, max = 64)
    @ApiModelProperty(value = "菜单名称", required = true)
    private String name;

    /**
     * 路由或接口地址
     */
    @Size(max = 255)
    @ApiModelProperty(value = "路由或接口地址")
    private String path;

    /**
     * 请求方式
     */
    @ApiModelProperty(value = "请求方式")
    private String method;

    /**
     * 角色
     */
    @ApiModelProperty(value = "哪些角色能访问菜单")
    private List<String> roles;
    //~methods
    //==================================================================================================================
}
