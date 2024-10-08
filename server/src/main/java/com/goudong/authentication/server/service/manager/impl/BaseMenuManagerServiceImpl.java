package com.goudong.authentication.server.service.manager.impl;

import com.goudong.authentication.common.util.CollectionUtil;
import com.goudong.authentication.common.util.tree.v2.Tree;
import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.domain.BaseRole;
import com.goudong.authentication.server.lang.RedisTool;
import com.goudong.authentication.server.rest.req.BaseMenuChangeSortNumReq;
import com.goudong.authentication.server.rest.req.BaseMenuCreateReq;
import com.goudong.authentication.server.rest.req.BaseMenuGetAllReq;
import com.goudong.authentication.server.rest.req.BaseMenuUpdateReq;
import com.goudong.authentication.server.rest.resp.BaseMenuGetAllResp;
import com.goudong.authentication.server.service.BaseMenuService;
import com.goudong.authentication.server.service.dto.ApiPermissionDTO;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseMenuManagerService;
import com.goudong.authentication.server.util.SecurityContextUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.goudong.authentication.server.enums.RedisKeyTemplateProviderEnum.APP_API_PERMISSION;

/**
 * 类描述：
 * 应用管理服务接口实现类
 * @author cfl
 * @version 1.0
 */
@Service
public class BaseMenuManagerServiceImpl implements BaseMenuManagerService {

    //~fields
    //==================================================================================================================
    /**
     * 菜单服务
     */
    @Resource
    private BaseMenuService baseMenuService;

    /**
     * redis工具
     */
    @Resource
    private RedisTool redisTool;

    //~methods
    //==================================================================================================================
    /**
     * 查询所有菜单
     * @param req 查询条件
     * @return 树形结构的菜单
     */
    @Override
    public BaseMenuGetAllResp getAll(BaseMenuGetAllReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long appId = myAuthentication.getRealAppId();
        req.setAppId(appId);
//        List<BaseMenuDTO> allByAppId = baseMenuService.findAllByAppId(appId);
        List<BaseMenuDTO> allByAppId = baseMenuService.findAll(req);

        BaseMenuGetAllResp baseMenuGetAllResp = new BaseMenuGetAllResp();
        if (CollectionUtil.isEmpty(allByAppId)) {
            return baseMenuGetAllResp;
        }
        // 排序
        allByAppId.sort(new Comparator<BaseMenuDTO>() {
            @Override
            public int compare(BaseMenuDTO o1, BaseMenuDTO o2) {
                return o1.getSortNum().compareTo(o2.getSortNum());
            }
        });
        List<BaseMenuDTO> tree = Tree.getInstance().toTree(allByAppId);

        baseMenuGetAllResp.setRecords(tree);
        return baseMenuGetAllResp;
    }

    /**
     * 新增菜单
     *
     * @param req 菜单参数
     * @return 新增后菜单
     */
    @Override
    public BaseMenuDTO save(BaseMenuCreateReq req) {
        return baseMenuService.save(req);
    }

    /**
     * 更新菜单
     *
     * @param req 被修改菜单
     * @return 修改后菜单
     */
    @Override
    public BaseMenuDTO update(BaseMenuUpdateReq req) {
        return baseMenuService.update(req);
    }

    /**
     * 删除菜单，如果菜单是父节点，就会删除它及它下面的所有子节点
     *
     * @param id
     * @return
     */
    @Override
    public Boolean deleteById(Long id) {
        return baseMenuService.deleteById(id);
    }

    /**
     * 批量删除菜单，及其所有子菜单
     *
     * @param ids 菜单id集合
     */
    @Override
    public void deleteByIds(List<Long> ids) {
        baseMenuService.deleteByIds(ids);
    }

    /**
     * 修改排序
     *
     * @param req
     * @return
     */
    @Override
    public Boolean changeSortNum(BaseMenuChangeSortNumReq req) {
        return baseMenuService.changeSortNum(req);
    }

    /**
     * 查询指定应用下的所有Api权限菜单（类型是按钮或接口）
     * @param appId 应用id
     * @return  菜单集合
     */
    @Override
    @Transactional
    public List<ApiPermissionDTO> listApiPermissionByAppId(Long appId) {
        String key = APP_API_PERMISSION.getFullKey(appId);
        if (Boolean.TRUE.equals(redisTool.hasKey(key))) {
            return redisTool.getList(APP_API_PERMISSION, ApiPermissionDTO.class, appId);
        }

        synchronized (this) {
            if (Boolean.TRUE.equals(redisTool.hasKey(key))) {
                return redisTool.getList(APP_API_PERMISSION, ApiPermissionDTO.class, appId);
            }

            List<BaseMenu> menus = baseMenuService.findAllByAppId(appId);
            if (CollectionUtil.isNotEmpty(menus)) {
                List<ApiPermissionDTO> apiPermissionDTOS = new ArrayList<>();
                menus.stream().filter(f -> f.getType() == BaseMenu.TypeEnum.BUTTON.getValue()
                        || f.getType() == BaseMenu.TypeEnum.API.getValue()).forEach(p -> {
                    ApiPermissionDTO dto = new ApiPermissionDTO();
                    dto.setId(p.getId());
                    dto.setName(p.getName());
                    dto.setPath(p.getPath());
                    dto.setMethod(p.getMethod());
                    dto.setRoles(p.getRoles().stream().map(BaseRole::getName).collect(Collectors.toList()));
                    apiPermissionDTOS.add(dto);
                });

                redisTool.set(APP_API_PERMISSION, apiPermissionDTOS, appId);

                return apiPermissionDTOS;
            }

            return new ArrayList<>(0);
        }
    }
}

