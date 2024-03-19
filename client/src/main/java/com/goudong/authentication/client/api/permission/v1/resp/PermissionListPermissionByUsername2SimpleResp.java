package com.goudong.authentication.client.api.permission.v1.resp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.goudong.authentication.client.core.MenuInterface;
import com.goudong.authentication.client.core.RoleInterface;
import com.goudong.authentication.client.core.TreeInterface;
import com.goudong.authentication.client.core.UserInterface;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * 类描述：
 * 获取用户权限列表响应-简化版
 * @author chenf
 */
@Data
public class PermissionListPermissionByUsername2SimpleResp implements UserInterface {
    //~fields
    //==================================================================================================================
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 激活状态：true 激活；false 未激活
     */
    private Boolean enabled;

    /**
     * 锁定状态：true 锁定；false 未锁定
     */
    private Boolean locked;

    /**
     * 有效截止时间
     */
    private Date validTime;

    /**
     * 角色信息
     */
    @JsonDeserialize(contentAs = RoleInner.class)
    private Collection<RoleInterface> roles;

    //~methods
    //==================================================================================================================
    /**
     * 类描述：
     * 角色
     * @author chenf
     */
    @Data
    public static class RoleInner implements RoleInterface {
        /**
         * 应用ID
         */
        private Long appId;
        /**
         * 角色ID
         */
        private Long id;

        /**
         * 角色名称
         */
        private String name;

        /**
         * 菜单信息
         */
        @JsonDeserialize(contentAs = MenuInner.class)
        private Collection<MenuInterface> menus;
    }

    /**
     * 类描述：
     * 菜单
     * @author chenf
     */
    @Data
    public static class MenuInner implements MenuInterface, TreeInterface<Long, Long, MenuInner>, Comparable<MenuInner>, Serializable {

        private static final long serialVersionUID = -4395163344388295463L;

        /**
         * 菜单id
         */
        private Long id;

        /**
         * 父级主键id
         */
        private Long parentId;

        /**
         * 应用id
         */
        private Long appId;

        /**
         * 权限标识
         */
        private String permissionId;

        /**
         * 菜单名称
         */
        private String name;

        /**
         * 菜单类型（1：菜单；2：按钮；3：接口）
         */
        private Integer type;

        /**
         * 路由或接口地址
         */
        private String path;

        /**
         * 请求方式
         */
        private String method;

        /**
         * 排序字段（值越小越靠前，仅仅针对前端路由）
         */
        private Integer sortNum;

        /**
         * 是否是隐藏菜单
         */
        private Boolean hide;

        /**
         * 前端菜单元数据
         */
        private String meta;

        /**
         * 备注
         */
        private String remark;

        /**
         * 子菜单
         */
        private Collection<MenuInner> children;

        @Override
        public void setChildren(Collection<MenuInner> children) {
            this.children = children;
        }

        @Override
        public int compareTo(MenuInner o) {
            return this.getSortNum().compareTo(o.getSortNum());
        }
    }
}
