package com.goudong.authentication.common.core;

import lombok.Data;

/**
 * 类描述：
 * 菜单
 * @author Administrator
 */
@Data
public class Menu {
    //~fields
    //==================================================================================================================
    /**
     * 菜单id
     */
    private Long id;

    /**
     * 上级菜单id
     */
    private Long parentId;

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
    //~methods
    //==================================================================================================================
}
