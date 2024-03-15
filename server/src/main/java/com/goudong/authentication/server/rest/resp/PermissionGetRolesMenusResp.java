package com.goudong.authentication.server.rest.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 获取
 * @author chenf
 */
@Data
public class PermissionGetRolesMenusResp implements Serializable {
    //~fields
    //==================================================================================================================
    private static final long serialVersionUID = 4713784610537488037L;

    /**
     * 角色集合
     */
    private List<RoleInner> roles;

    //~methods
    //==================================================================================================================
    /**
     * 类描述：
     * 角色
     * @author chenf
     */
    @Data
    public static class RoleInner {
        /**
         * 应用ID
         */
        @ApiModelProperty(value = "应用ID", required = true)
        private Long appId;

        /**
         * 角色ID
         */
        @ApiModelProperty(value = "角色ID", required = true)
        private Long id;

        /**
         * 角色名称
         */
        @ApiModelProperty(value = "角色名称", required = true)
        private String name;

        /**
         * 角色拥有的权限列表
         */
        @ApiModelProperty(value = "角色拥有的权限列表", required = true)
        private List<MenuInner> menus;
    }

    /**
     * 类描述：
     * 菜单
     * @author chenf
     */
    @Data
    public static class MenuInner {

        /**
         * 应用ID
         */
        @ApiModelProperty(value = "应用ID", required = true)
        private Long appId;

        /**
         * 菜单ID
         */
        @ApiModelProperty(value = "菜单ID", required = true)
        private Long id;

        /**
         * 父级主键id
         */
        @ApiModelProperty(value = "父级主键id")
        private Long parentId;

        /**
         * 权限标识
         */
        @ApiModelProperty(value = "权限标识", required = true)
        private String permissionId;

        /**
         * 菜单名称
         */
        @ApiModelProperty(value = "菜单名称", required = true)
        private String name;

        /**
         * 菜单类型（1：菜单；2：按钮；3：接口）
         */
        @ApiModelProperty(value = "菜单类型（1：菜单；2：按钮；3：接口）", required = true)
        private Integer type;

        /**
         * 路由或接口地址
         */
        @ApiModelProperty(value = "路由或接口地址")
        private String path;

        /**
         * 请求方式
         */
        @ApiModelProperty(value = "请求方式")
        private String method;

        /**
         * 排序字段（值越小越靠前，仅仅针对前端路由）
         */
        @ApiModelProperty(value = "排序字段（值越小越靠前，仅仅针对前端路由）")
        private Integer sortNum;

        /**
         * 是否是隐藏菜单
         */
        @ApiModelProperty(value = "是否是隐藏菜单", required = true)
        private Boolean hide;

        /**
         * 前端菜单元数据
         */
        @ApiModelProperty(value = "前端菜单元数据")
        private String meta;

        /**
         * 备注
         */
        @ApiModelProperty(value = "备注")
        private String remark;
    }
}
