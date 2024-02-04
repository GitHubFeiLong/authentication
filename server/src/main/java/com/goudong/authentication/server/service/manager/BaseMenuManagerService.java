package com.goudong.authentication.server.service.manager;

import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.rest.req.BaseMenuChangeSortNumReq;
import com.goudong.authentication.server.rest.req.BaseMenuCreateReq;
import com.goudong.authentication.server.rest.req.BaseMenuGetAllReq;
import com.goudong.authentication.server.rest.req.BaseMenuUpdateReq;
import com.goudong.authentication.server.rest.resp.BaseMenuGetAllResp;
import com.goudong.authentication.server.service.dto.ApiPermissionDTO;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;

import java.util.List;

/**
 * 类描述：
 * 应用管理服务接口
 * @author cfl
 * @version 1.0
 */
public interface BaseMenuManagerService {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 查询所有菜单
     * @param req 查询条件
     * @return 树形结构的菜单
     */
    BaseMenuGetAllResp getAll(BaseMenuGetAllReq req);

    /**
     * 新增菜单
     * @param req 菜单参数
     * @return 新增后菜单
     */
    BaseMenuDTO save(BaseMenuCreateReq req);

    /**
     * 更新菜单
     * @param req 被修改菜单
     * @return 修改后菜单
     */
    BaseMenuDTO update(BaseMenuUpdateReq req);

    /**
     * 删除菜单，如果菜单是父节点，就会删除它及它下面的所有子节点
     * @param id
     * @return
     */
    Boolean deleteById(Long id);

    /**
     * 批量删除菜单，及其所有子菜单
     * @param ids   菜单id集合
     */
    void deleteByIds(List<Long> ids);

    /**
     * 修改排序
     * @param req
     * @return
     */
    Boolean changeSortNum(BaseMenuChangeSortNumReq req);

    /**
     * 查询指定应用下的所有Api权限菜单（类型是按钮或接口）
     * @param appId 应用id
     * @return  API权限集合
     */
    List<ApiPermissionDTO> listApiPermissionByAppId(Long appId);

}
