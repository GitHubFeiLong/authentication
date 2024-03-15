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
     * 获取用户ID
     * @return 用户ID
     */
    Long getId();

    /**
     * 用户名称
     * @return 获取用户名称
     */
    String getUsername();

    /**
     * 获取激活状态
     * @return 激活状态：true 激活；false 未激活
     */
    Boolean getEnabled();

    /**
     * 获取锁定状态
     * @return 锁定状态：true 锁定；false 未锁定
     */
    Boolean getLocked();

    /**
     * @return 账户有效日期时间
     */
    Date getValidTime();

    /**
     * 菜单信息
     */
    Collection<? extends RoleInterface> getRoles();
}
