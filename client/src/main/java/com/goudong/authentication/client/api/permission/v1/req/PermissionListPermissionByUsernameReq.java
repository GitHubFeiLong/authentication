package com.goudong.authentication.client.api.permission.v1.req;

import lombok.Data;


/**
 * 类描述：
 * 获取用户权限列表的请求对象
 * @author chenf
 */
@Data
public class PermissionListPermissionByUsernameReq {
    //~fields
    //==================================================================================================================
    /**
     * 用户名 username不能为空
     */
    private String username;
    //~methods
    //==================================================================================================================
}
