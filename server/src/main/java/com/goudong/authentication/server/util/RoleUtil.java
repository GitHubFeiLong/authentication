package com.goudong.authentication.server.util;

import com.goudong.authentication.common.constant.CommonConst;

import java.util.Objects;

/**
 * 类描述：
 * 角色工具类
 * @author chenf
 */
public class RoleUtil {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 校验角色是否是超级管理员
     * @param roleName  角色名称
     * @return true：是超级管理员；false：不是超级管理员
     */
    public static boolean isSuperAdmin(String roleName) {
        return Objects.equals(roleName, CommonConst.ROLE_APP_SUPER_ADMIN);
    }

    /**
     * 校验角色是否是管理员
     * @param roleName  角色名称
     * @return true：是管理员；false：不是管理员
     */
    public static boolean isAdmin(String roleName) {
        return Objects.equals(roleName, CommonConst.ROLE_APP_SUPER_ADMIN) || Objects.equals(roleName, CommonConst.ROLE_APP_ADMIN);
    }

    /**
     * 校验角色是否是普通角色
     * @param roleName  角色名称
     * @return true：普通角色；false：管理员
     */
    public static boolean isOrdinaryRole(String roleName) {
        return !isAdmin(roleName);
    }
}
