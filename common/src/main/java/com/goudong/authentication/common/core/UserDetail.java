package com.goudong.authentication.common.core;

import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * 类描述：
 *
 * @author Administrator
 */
@Data
public class UserDetail {
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
     * 应用id
     */
    private Long realAppId;

    /**
     * 应用名称
     */
    private String realAppName;

    /**
     * 应用主页
     */
    private String realAppHome;

    /**
     * 用户名
     */
    private String username;

    /**
     * 角色
     */
    private List<String> roles;

    /**
     * 菜单
     */
    private List<Menu> menus;
    //~methods
    //==================================================================================================================
}
