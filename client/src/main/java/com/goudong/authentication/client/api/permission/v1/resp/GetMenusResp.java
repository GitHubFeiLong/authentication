package com.goudong.authentication.client.api.permission.v1.resp;

import com.goudong.authentication.client.dto.BaseMenuDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 获取所有菜单
 * @author cfl
 * @version 1.0
 */
@Data
public class GetMenusResp implements Serializable{

    private static final long serialVersionUID = -4817210046984660585L;
    /**
     * 菜单集合
     */
    private List<BaseMenuDTO> menus;

}