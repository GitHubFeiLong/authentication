package com.goudong.authentication.client.api.permission.v1.resp;

import com.goudong.authentication.client.dto.BaseRoleDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 获取角色菜单信息响应对象
 * @author chenf
 */
@Data
public class GetRolesMenusResp implements Serializable {
    //~fields
    //==================================================================================================================
    private static final long serialVersionUID = 5822475474436272096L;

    /**
     * 角色集合
     */
    private List<BaseRoleDTO> roles;
    //~methods
    //==================================================================================================================
}
