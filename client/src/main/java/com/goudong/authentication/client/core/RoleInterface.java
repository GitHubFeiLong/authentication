package com.goudong.authentication.client.core;

import java.util.Collection;

/**
 * 类描述：
 * 角色接口，定义角色对象基本方法
 * @author chenf
 */
public interface RoleInterface {

    /**
     * @return 角色所在的应用ID
     */
    Long getAppId();

    /**
     * @return 角色的ID
     */
    Long getId();

    /**
     * @return 角色的名称
     */
    String getName();

    /**
     * @return 菜单信息
     */
    Collection<? extends MenuInterface> getMenus();
}
