package com.goudong.authentication.common.core;

import com.goudong.authentication.common.constant.CommonConst;
import lombok.Data;

import java.util.List;
import java.util.Map;
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

    /**
     * 用户其它信息
     */
    private Map<String, Object> detail;

    //~methods
    //==================================================================================================================

    /**
     * 无参构造
     */
    public UserSimple() {
    }

    /**
     * 有参构造
     * @param id        用户id
     * @param appId     用户appId
     * @param realAppId 用户realAppId
     * @param username  用户名
     * @param roles     用户角色集合
     */
    public UserSimple(Long id, Long appId, Long realAppId, String username, List<String> roles) {
        this.id = id;
        this.appId = appId;
        this.realAppId = realAppId;
        this.username = username;
        this.roles = roles;
    }

    /**
     * 是否是超级管理员
     * @return true 超级管理员，false 不是超级管理员
     */
    public boolean superAdmin() {
        return this.roles.stream().anyMatch(f -> Objects.equals(f, CommonConst.ROLE_APP_SUPER_ADMIN));
    }

    /**
     * 判断登录用户，是否是管理员或超级管理员
     * @return true 管理员，false 普通用户
     */
    public boolean admin() {
        return this.roles.stream().anyMatch(f -> Objects.equals(f, CommonConst.ROLE_APP_ADMIN) || Objects.equals(f, CommonConst.ROLE_APP_SUPER_ADMIN));
    }
}
