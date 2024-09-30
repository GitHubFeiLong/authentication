package com.goudong.authentication.server.service.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.goudong.authentication.common.util.AssertUtil;
import com.goudong.authentication.common.util.CollectionUtil;
import com.goudong.authentication.common.util.tree.v2.Tree;
import com.goudong.authentication.server.constant.CommonConst;
import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.domain.BaseRole;
import com.goudong.authentication.server.exception.BasicException;
import com.goudong.authentication.server.exception.ClientException;
import com.goudong.authentication.server.exception.ClientExceptionEnum;
import com.goudong.authentication.server.lang.PageResult;
import com.goudong.authentication.server.lang.RedisTool;
import com.goudong.authentication.server.rest.req.BaseRoleChangePermissionReq;
import com.goudong.authentication.server.rest.req.BaseRoleCreateReq;
import com.goudong.authentication.server.rest.req.BaseRolePageReq;
import com.goudong.authentication.server.rest.req.BaseRoleUpdateReq;
import com.goudong.authentication.server.rest.req.search.BaseRoleDropDownReq;
import com.goudong.authentication.server.rest.resp.BaseRoleDropDownResp;
import com.goudong.authentication.server.rest.resp.BaseRolePageResp;
import com.goudong.authentication.server.rest.resp.BaseRolePermissionListResp;
import com.goudong.authentication.server.service.BaseMenuService;
import com.goudong.authentication.server.service.BaseRoleService;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.authentication.server.service.dto.BaseRoleDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseRoleManagerService;
import com.goudong.authentication.server.service.mapper.BaseMenuMapper;
import com.goudong.authentication.server.util.RoleUtil;
import com.goudong.authentication.server.util.SecurityContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.goudong.authentication.server.enums.RedisKeyTemplateProviderEnum.APP_API_PERMISSION;

/**
 * 类描述：
 * 角色管理服务层接口实现类
 * @author chenf
 */
@Service
@Slf4j
public class BaseRoleManagerServiceImpl implements BaseRoleManagerService {
    //~fields
    //==================================================================================================================
    /**
     * 角色服务接口
     */
    @Resource
    private BaseRoleService baseRoleService;

    /**
     * 菜单服务接口
     */
    @Resource
    private BaseMenuService baseMenuService;

    /**
     * BaseMenu映射器
     */
    @Resource
    private BaseMenuMapper baseMenuMapper;

    /**
     * redis工具
     */
    @Resource
    private RedisTool redisTool;

    //~methods
    //==================================================================================================================
    /**
     * 用户所在应用下的角色下拉
     *
     * @param req 条件查询参数
     * @return 角色下拉分页对象
     */
    @Override
    public PageResult<BaseRoleDropDownResp> roleDropDown(BaseRoleDropDownReq req) {
        return baseRoleService.roleDropDown(req);
    }

    /**
     * 分页查询角色列表
     *
     * @param req 条件查询参数
     * @return 角色分页列表
     */
    @Override
    public PageResult<BaseRolePageResp> page(BaseRolePageReq req) {
        return baseRoleService.page(req);
    }

    /**
     * 保存角色
     *
     * @param req 角色信息
     * @return 保存后对象
     */
    @Override
    public BaseRoleDTO save(BaseRoleCreateReq req) {
        return baseRoleService.save(req);
    }

    /**
     * 修改角色
     *
     * @param req 需要修改的角色信息
     * @return 修改后角色信息
     */
    @Override
    public BaseRoleDTO update(BaseRoleUpdateReq req) {
        return baseRoleService.update(req);
    }

    /**
     * 批量删除角色
     *
     * @param ids 删除的id集合
     * @return true删除成功；false删除失败
     */
    @Override
    public Boolean deleteByIds(List<Long> ids) {
        return baseRoleService.deleteByIds(ids);
    }

    /**
     * 查询角色id对应的权限信息
     *
     * @param id 角色id
     * @return 角色信息和权限信息
     */
    @Override
    @Transactional(readOnly = true)
    public BaseRolePermissionListResp getPermissionListById(Long id) {
        BaseRole rolePO = baseRoleService.findById(id);
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        AssertUtil.isEquals(rolePO.getAppId(), myAuthentication.getRealAppId(), () -> ClientException.clientByForbidden());
        // 当前用户所拥有的菜单权限，不能越级设置权限
        List<BaseMenuDTO> permissions;
        List<Long> menuIds;
        BaseRolePermissionListResp resp = BeanUtil.copyProperties(rolePO, BaseRolePermissionListResp.class);

        // 用户的两个应用id属性一致
        if (Objects.equals(myAuthentication.getAppId(), myAuthentication.getRealAppId())
                && Objects.equals(CommonConst.AUTHENTICATION_SERVER_APP_ID, myAuthentication.getRealAppId())) {
            log.debug("用户是真正的认证服务的用户，查询认证服务下的所有菜单");
            permissions = baseMenuMapper.toDto(baseMenuService.findAllByAppId(CommonConst.AUTHENTICATION_SERVER_APP_ID));
        } else {
            log.debug("用户不是真正的认证服务的用户，可能是其他应用下的用户，查询两个应用下的菜单");
            // 查询认证服务的菜单
            List<BaseMenu> baseMenusByAppId = baseMenuService.findAllByAppId(CommonConst.AUTHENTICATION_SERVER_APP_ID);
            // 排除不需要显示的菜单
            baseMenusByAppId = baseMenusByAppId.stream()
                    .filter(f -> !CommonConst.ROLE_APP_ADMIN_IGNORE_MENUS.contains(f.getId())
                            && !CommonConst.ROLE_APP_ADMIN_IGNORE_MENUS.contains(f.getParentId())
                    ).collect(Collectors.toList());
            // 查询自己应用下的所有菜单
            List<BaseMenu> baseMenusByRealAppId = baseMenuService.findAllByAppId(myAuthentication.getRealAppId());
            List<BaseMenu> all = new ArrayList<>(baseMenusByAppId.size() + baseMenusByRealAppId.size());
            all.addAll(baseMenusByAppId);
            all.addAll(baseMenusByRealAppId);
            // 转dto
            permissions = baseMenuMapper.toDto(all);
        }

        // 角色拥有的权限
        log.debug("获取角色拥有的权限");
        if (RoleUtil.isAdmin(rolePO.getName())) {
            log.debug("管理员拥有所有权限");
            menuIds = permissions.stream().map(BaseMenuDTO::getId).collect(Collectors.toList());
        } else {
            // 角色拥有的权限
            log.debug("普通角色拥有已设置的权限");
            menuIds = rolePO.getMenus().stream().map(BaseMenu::getId).collect(Collectors.toList());
        }

        // 拥有的权限
        permissions.forEach(p -> {
            if (menuIds.contains(p.getId())) {
                p.setChecked(true);
            }
        });

        // 转换成Tree
        resp.setPermission(Tree.getInstance().toTree(permissions));

        return resp;
    }

    /**
     * 修改角色权限
     *
     * @param req 被修改的角色和需要设置的权限信息
     * @return true修改成功；false修改失败
     */
    @Override
    @Transactional
    public Boolean changePermission(BaseRoleChangePermissionReq req) {
        BaseRole rolePO = baseRoleService.findById(req.getId());
        // 校验角色是否能修改权限，保留角色不能修改（比如 ROLE_APP_SUPER_ADMIN、ROLE_APP_ADMIN）
        Assert.isFalse(Objects.equals(rolePO.getName(), CommonConst.ROLE_APP_SUPER_ADMIN)
                || Objects.equals(rolePO.getName(), CommonConst.ROLE_APP_ADMIN), () -> BasicException.builder()
                .exceptionEnum(ClientExceptionEnum.BAD_REQUEST)
                .clientMessageTemplate("不能修改【{}】角色的权限")
                .clientMessageParams(rolePO.getName())
                .build());

        MyAuthentication myAuthentication = SecurityContextUtil.get();
        AssertUtil.isEquals(rolePO.getAppId(), myAuthentication.getRealAppId(), () -> ClientException.clientByForbidden());
        if (CollectionUtil.isEmpty(req.getMenuIds())) {
            rolePO.setMenus(new ArrayList<>(0));
            return true;
        }

        List<BaseMenu> menus = baseMenuService.findAllById(req.getMenuIds());
        // 设置的菜单权限是否是认证服务的菜单
        AtomicBoolean isNeedDeleteAuthenticationServerCache = new AtomicBoolean(false);
        // 校验用户是否能设置菜单
        menus.forEach(menu -> {
            // 还未标记true && 角色不是认证服务 && 菜单属于认证服务
            if (!isNeedDeleteAuthenticationServerCache.get()
                    && !Objects.equals(CommonConst.AUTHENTICATION_SERVER_APP_ID, rolePO.getAppId())
                    && Objects.equals(menu.getAppId(), CommonConst.AUTHENTICATION_SERVER_APP_ID)) {
                isNeedDeleteAuthenticationServerCache.set(true);
            }
            AssertUtil.isTrue(
                    Objects.equals(menu.getAppId(), CommonConst.AUTHENTICATION_SERVER_APP_ID)
                            || Objects.equals(menu.getAppId(), myAuthentication.getAppId())
                            || Objects.equals(menu.getAppId(), myAuthentication.getRealAppId()),
                    () -> BasicException.builder()
                            .exceptionEnum(ClientExceptionEnum.BAD_REQUEST)
                            .clientMessageTemplate("不能设置【{}】菜单的权限")
                            .clientMessageParams(menu.getName())
                            .build());
        });

        rolePO.setMenus(menus);

        // 删除应用的权限缓存
        log.info("删除应用权限缓存：{}", rolePO.getAppId());
        redisTool.deleteKey(APP_API_PERMISSION, rolePO.getAppId());
        // 有修改认证服务的权限
        if (isNeedDeleteAuthenticationServerCache.get()) {
            log.debug("本次修改了认证服务的菜单权限，需要删除认证服务的权限缓存");
            redisTool.deleteKey(APP_API_PERMISSION, CommonConst.AUTHENTICATION_SERVER_APP_ID);
        }

        return true;
    }

}
