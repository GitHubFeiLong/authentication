package com.goudong.authentication.client.core;

import lombok.Data;

import java.util.Collection;

/**
 * 类描述：
 * 角色基本信息
 * @author chenf
 */
@Data
public class BaseRole implements RoleInterface{
    //~fields
    //==================================================================================================================
    /**
     * 角色所在的应用ID
     */
    private Long appId;
    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 菜单信息
     */
    private Collection<? extends MenuInterface> menus;
    //~methods
    //==================================================================================================================
}
