package com.goudong.authentication.client.core;

import java.util.Collection;
import java.util.Date;

/**
 * 类描述：
 * 用户接口
 * @author chenf
 */
public interface UserInterface {

    /**
     * @return 获取用户ID
     */
    Long getId();

    /**
     * 设置用户ID
     * @param id 用户ID
     */
    void setId(Long id);

    /**
     * @return 获取用户名称
     */
    String getUsername();

    /**
     * 设置用户名称
     * @param username 用户名称
     */
    void setUsername(String username);

    /**
     * @return 获取激活状态：true 激活；false 未激活
     */
    Boolean getEnabled();

    /**
     * 设置激活状态
     * @param enabled true 激活；false 未激活
     */
    void setEnabled(Boolean enabled);

    /**
     * @return 获取锁定状态：true 锁定；false 未锁定
     */
    Boolean getLocked();

    /**
     * 设置锁定状态
     * @param locked true 锁定；false 未锁定
     */
    void setLocked(Boolean locked);

    /**
     * @return 账户有效日期时间
     */
    Date getValidTime();

    /**
     * 设置有效日期时间
     * @param validTime 有效日期时间
     */
    void setValidTime(Date validTime);

    /**
     * @return 获取权限信息
     */
    Collection<? extends RoleInterface> getRoles();

    /**
     * 设置权限信息
     * @param roles 权限信息
     */
    void setRoles(Collection<? extends RoleInterface> roles);
}
