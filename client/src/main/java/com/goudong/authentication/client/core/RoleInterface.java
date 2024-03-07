package com.goudong.authentication.client.core;

import java.util.Collection;

/**
 * 类描述：
 * 角色接口，定义角色对象基本方法
 * @author chenf
 */
public interface RoleInterface {

    /**
     * 角色ID
     */
    Long getId();

    /**
     * 角色名称
     */
    String getName();

    /**
     * 菜单信息
     */
    Collection<? extends MenuInterface> getMenus();
}
