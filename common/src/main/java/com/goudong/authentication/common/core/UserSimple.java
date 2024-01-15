package com.goudong.authentication.common.core;

import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * 类描述：
 * 用户简单信息
 * @author chenf
 */
@Data
public class UserSimple {
    //~fields
    //==================================================================================================================
    /**
     * 用户id
     */
    private Long id;

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 真实应用id（例如xx应用管理员，app_id是认证服务应用的app_id，但是real_app_id是自己所管理xx应用的app_id）
     */
    private Long realAppId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 角色
     */
    private List<String> roles;

    //~methods
    //==================================================================================================================
    public UserSimple() {
    }

    public UserSimple(Long id, Long appId, Long realAppId, String username, List<String> roles) {
        this.id = id;
        this.appId = appId;
        this.realAppId = realAppId;
        this.username = username;
        this.roles = roles;
    }
}
