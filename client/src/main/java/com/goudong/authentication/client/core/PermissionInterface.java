package com.goudong.authentication.client.core;

import java.util.Collection;

/**
 * 类描述：
 * 权限接口：
 * <pre>
 *     1. 获取用户所有权限
 *     2. 判断请求是否有权限
 * </pre>
 * @author chenf
 */
public interface PermissionInterface {

    /**
     * 获取用户的所有角色权限
     * @param username 用户名
     * @return
     */
    Collection<? extends RoleInterface> getRoles(String username);

    /**
     * 判断用户是否有访问资源的权限
     * @param username  用户名
     * @param resource  资源
     * @return true：有权限，false：无权限
     */
    boolean hasPermission(String username, Resource resource);
}
