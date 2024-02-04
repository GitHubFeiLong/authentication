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
     * 用户真实appId
     */
    private Long realAppId;

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


