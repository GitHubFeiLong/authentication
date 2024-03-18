package com.goudong.authentication.client.core;


import java.util.Collection;

/**
 * 类描述：
 * 角色接口，定义角色对象基本方法
 * @author chenf
 */
public interface RoleInterface {

    /**
     * @return 获取应用ID
     */
    Long getAppId();

    /**
     * 设置应用ID
     * @param appId 应用ID
     */
    void setAppId(Long appId);

    /**
     * @return 角色的ID
     */
    Long getId();

    /**
     * 设置ID
     * @param id   角色ID
     */
    void setId(Long id);

    /**
     * @return 角色的名称
     */
    String getName();

    /**
     * 设置角色名称
     * @param name  角色名称
     */
    void setName(String name);

    /**
     * @return 菜单信息
     */
    Collection<? extends MenuInterface> getMenus();

    /**
     * 设置菜单权限
     * @param menus 菜单权限
     */
    void setMenus(Collection<? extends MenuInterface> menus);
}
