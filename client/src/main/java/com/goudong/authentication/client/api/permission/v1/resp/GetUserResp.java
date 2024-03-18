package com.goudong.authentication.client.api.permission.v1.resp;

import com.goudong.authentication.client.core.RoleInterface;
import com.goudong.authentication.client.core.UserInterface;
import lombok.Data;

import java.util.Collection;
import java.util.Date;

/**
 * 类描述：
 * 获取用户接口响应对象
 * @author chenf
 */
@Data
public class GetUserResp implements UserInterface {
    //~fields
    //==================================================================================================================
    /**
     * 应用id
     */
    private Long appId;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 激活状态：true 激活；false 未激活
     */
    private Boolean enabled;

    /**
     * 锁定状态：true 锁定；false 未锁定
     */
    private Boolean locked;

    /**
     * 有效截止时间
     */
    private Date validTime;

    /**
     * 角色
     */
    private Collection<? extends RoleInterface> roles;

    //~methods
    //==================================================================================================================


}
