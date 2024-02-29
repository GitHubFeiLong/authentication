package com.goudong.authentication.server.service.manager;

import com.goudong.authentication.server.rest.req.PermissionListPermissionByUsername2SimpleResp;
import com.goudong.authentication.server.rest.req.PermissionListPermissionByUsernameReq;
import com.goudong.authentication.server.service.dto.PermissionDTO;

import java.util.List;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
public interface PermissionManagerService {
    /**
     * 查询权限列表
     * @return 权限列表
     */
    List<PermissionDTO> listPermission();

    /**
     * 检查是否有权限
     * @return
     */
    Boolean checkPermission();

    /**
     * 根据用户名获取他拥有的所有权限
     * @param req 获取用户权限的参数
     * @return 用户拥有的所有角色和权限
     */
    PermissionListPermissionByUsername2SimpleResp listPermissionByUsername2Simple(PermissionListPermissionByUsernameReq req);
}
