package com.goudong.authentication.client.constant;

/**
 * 类描述：
 * api接口的地址，不包含上下文
 * @author chenf
 */
public class ApiConst {
    //~user apis
    //==================================================================================================================
    /**
     * 给指定用户创建token
     */
    public static final String USER_CREATE_TOKEN = "/user/create-token";

    /**
     * 刷新token
     */
    public static final String USER_REFRESH_TOKEN = "/user/refresh-token";

    /**
     * POST请求，简单新增用户
     */
    public static final String USER_SIMPLE_CREATE_USER = "/user/base-user/simple-create";

    /**
     * 批量删除用户
     */
    public static final String USER_DELETE_USERS = "/user/base-users";

    /**
     * 补充token，返回新的令牌
     */
    public static final String USER_SUPPLEMENT_TOKEN = "/user/supplement-token";

    //~permission apis
    //==================================================================================================================
    /**
     * 获取应用配置的所有菜单
     */
    public static final String PERMISSION_GET_MENUS = "/permission/get-menus";

    /**
     * 获取应用的所有角色及角色对应的菜单权限
     */
    public static final String PERMISSION_GET_ROLES_MENUS = "/permission/get-roles-menus";

    /**
     * 获取应用的用户和角色信息
     */
    public static final String PERMISSION_GET_USER = "/permission/get-user";

    /**
     * 获取指定用户权限（角色、角色权限）
     */
    @Deprecated
    public static final String PERMISSION_LIST_BY_USERNAME = "/permission/list-by-username/simple";

    //~methods
    //==================================================================================================================
}
