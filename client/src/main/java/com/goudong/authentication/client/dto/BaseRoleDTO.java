package com.goudong.authentication.client.dto;

import com.goudong.authentication.client.core.MenuInterface;
import com.goudong.authentication.client.core.RoleInterface;
import lombok.Data;

import java.util.Collection;

/**
 * 类描述：
 * 角色信息
 * @author chenf
 */
@Data
public class BaseRoleDTO implements RoleInterface {
    //~fields
    //==================================================================================================================
    /**
     * 应用ID
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
     * 角色拥有的菜单权限
     */
    private Collection<? extends MenuInterface> menus;

    //~methods
    //==================================================================================================================


}
