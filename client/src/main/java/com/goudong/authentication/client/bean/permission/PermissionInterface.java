package com.goudong.authentication.client.bean.permission;

import com.goudong.authentication.client.core.MenuInterface;
import com.goudong.authentication.client.core.Resource;
import com.goudong.authentication.client.core.RoleInterface;
import com.goudong.authentication.client.core.UserInterface;

import java.util.Collection;

/**
 * 类描述：
 * 用户权限相关功能
 * @author chenf
 */
public interface PermissionInterface {

    /**
     * 获取应用配置的所有权限
     * @param appId 应用ID，当参数{@code appId}值为null时，内部使用默认应用ID
     * @return  应用的所有菜单
     */
    Collection<MenuInterface> getMenus(Long appId);

    /**
     * 获取应用配置的所有角色和菜单
     * @param appId 应用ID，当参数{@code appId}值为null时，内部使用默认应用ID
     * @return  应用的所有角色信息及角色对应的菜单
     */
    Collection<RoleInterface> getRolesMenus(Long appId);

    /**
     * 获取用户信息
     * @param appId     应用ID，当参数{@code appId}值为null时，内部使用默认应用ID
     * @param username  用户名
     * @return  用户信息，包含用户基本信息和拥有的角色信息
     */
    UserInterface getUser(Long appId, String username);

    /**
     * 获取用户信息（包含菜单权限）
     * @param appId     应用ID，当参数{@code appId}值为null时，内部使用默认应用ID
     * @param username  用户名
     * @return  用户信息，包含用户基本信息和拥有的角色权限信息
     */
    UserInterface getUserDetail(Long appId, String username);

    /**
     * 检查用户是否有访问资源的权限
     * @param appId     应用ID，当参数{@code appId}值为null时，内部使用默认应用ID
     * @param username  用户名
     * @param resource  资源
     * @return true：有权限，false：无权限
     */
    boolean checkAccessRight(Long appId, String username, Resource resource);

    /**
     * 清理缓存
     * @param appId 应用ID，当参数appId值为null时，内部使用默认应用ID
     */
    void clean(Long appId);
}
