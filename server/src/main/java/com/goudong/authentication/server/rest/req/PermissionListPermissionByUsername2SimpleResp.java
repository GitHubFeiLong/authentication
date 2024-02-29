package com.goudong.authentication.server.rest.req;

import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.core.util.tree.v2.TreeInterface;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 获取用户权限列表响应-简化版
 * @author chenf
 */
@Data
public class PermissionListPermissionByUsername2SimpleResp {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("角色信息")
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
        @ApiModelProperty("角色名称")
        private String name;

        @ApiModelProperty("菜单信息")
        private List<MenuInner> menus;
    }

    /**
     * 类描述：
     * 菜单
     * @author chenf
     */
    @Data
    public static class MenuInner implements TreeInterface<Long, Long, MenuInner>, Comparable<MenuInner>, Serializable {

        private static final long serialVersionUID = -4395163344388295463L;

        @ApiModelProperty("菜单id")
        private Long id;

        @ApiModelProperty("父级主键id")
        private Long parentId;

        @ApiModelProperty("应用id")
        private Long appId;

        @ApiModelProperty("权限标识")
        private String permissionId;

        @ApiModelProperty("菜单名称")
        private String name;

        @ApiModelProperty("菜单类型（1：菜单；2：按钮；3：接口）")
        private Integer type;

        @ApiModelProperty("路由或接口地址")
        private String path;

        @ApiModelProperty("请求方式")
        private String method;

        @ApiModelProperty("排序字段（值越小越靠前，仅仅针对前端路由）")
        private Integer sortNum;

        @ApiModelProperty("是否是隐藏菜单")
        private Boolean hide;

        @ApiModelProperty("前端菜单元数据")
        private String meta;

        @ApiModelProperty("备注")
        private String remark;

        @ApiModelProperty(value = "子菜单")
        private List<MenuInner> children;

        @Override
        public void setChildren(List children) {
            this.children = children;
        }

        @Override
        public int compareTo(MenuInner o) {
            return this.getSortNum().compareTo(o.getSortNum());
        }
    }
}
