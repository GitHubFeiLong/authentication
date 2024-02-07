package com.goudong.authentication.common.core;

import lombok.Data;

import java.util.List;

/**
 * 类描述：
 * 登录返回信息
 * @author cfl
 */
@Data
public class LoginResp {
    //~fields
    //==================================================================================================================
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户appId
     */
    private Long appId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 用户真实appId
     */
    private Long realAppId;

    /**
     * 应用名称
     */
    private String realAppName;

    /**
     * 用户名
     */
    private String username;

    /**
     * 令牌
     */
    private Token token;

    /**
     * 角色列表
     */
    private List<String> roles;

    /**
     * 应用的首页地址
     */
    private String homePage;

    /**
     * 应用的首页地址
     */
    private String realHomePage;

    //~methods
    //==================================================================================================================
}


