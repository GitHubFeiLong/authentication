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
     * 用户：简单新增用户
     */
    public static final String USER_SIMPLE_CREATE_USER = "/user/base-user/simple-create";

    /**
     * 批量删除用户
     */
    public static final String USER_DELETE_USERS = "/user/base-users";


    //~permission apis
    //==================================================================================================================
    /**
     * 获取指定用户权限（角色、角色权限）
     */
    public static final String PERMISSION_LIST_BY_USERNAME = "/permission/list-by-username/simple";

    //~methods
    //==================================================================================================================
}
