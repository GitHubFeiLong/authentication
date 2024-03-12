package com.goudong.authentication.client.core;

import java.util.Collection;
import java.util.List;

/**
 * 类描述：
 * 菜单接口，定义菜单基本方法
 * @author chenf
 */
public interface MenuInterface {
    /**
     * 菜单id
     */
    Long getId();

    /**
     * 父级主键id
     */
    Long getParentId();

    /**
     * 权限标识
     */
    String getPermissionId();

    /**
     * 菜单名称
     */
    String getName();

    /**
     * 菜单类型（1：菜单；2：按钮；3：接口）
     */
    Integer getType();

    /**
     * 路由或接口地址
     */
    String getPath();

    /**
     * 请求方式，JSON数组，格式类似是：["GET","POST"]
     */
    String getMethod();

    /**
     * 子菜单
     */
    Collection<? extends MenuInterface> getChildren();
}
