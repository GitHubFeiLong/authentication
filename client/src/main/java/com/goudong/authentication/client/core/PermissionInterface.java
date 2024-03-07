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
     * 获取用户的所有
     * @return
     */
    Collection<? extends RoleInterface> getRoles();
}
