package com.goudong.authentication.client.api.permission.v1.req;

import com.goudong.authentication.client.core.BaseApiReq;
import lombok.*;
import lombok.experimental.SuperBuilder;


/**
 * 类描述：
 * 获取用户权限列表的请求对象
 * @author chenf
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionListPermissionByUsernameReq extends BaseApiReq {
    //~fields
    //==================================================================================================================
    /**
     * 用户名 username不能为空
     */
    private String username;
    //~methods
    //==================================================================================================================
}
