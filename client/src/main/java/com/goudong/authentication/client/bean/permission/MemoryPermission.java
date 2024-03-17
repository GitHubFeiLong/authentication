package com.goudong.authentication.client.bean.permission;

import com.goudong.authentication.client.api.permission.v1.PermissionV1Api;
import com.goudong.authentication.client.api.permission.v1.req.GetMenusReq;
import com.goudong.authentication.client.api.permission.v1.req.GetRolesMenusReq;
import com.goudong.authentication.client.api.permission.v1.req.GetUserReq;
import com.goudong.authentication.client.api.permission.v1.resp.GetMenusResp;
import com.goudong.authentication.client.api.permission.v1.resp.GetRolesMenusResp;
import com.goudong.authentication.client.api.permission.v1.resp.GetUserResp;
import com.goudong.authentication.client.core.MenuInterface;
import com.goudong.authentication.client.core.Resource;
import com.goudong.authentication.client.core.RoleInterface;
import com.goudong.authentication.client.core.UserInterface;
import com.goudong.authentication.client.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.*;

/**
 * 类描述：
 * 使用内存的存储
 * @author cfl
 * @version 1.0
 */
@Slf4j
@Component
public class MemoryPermission implements PermissionInterface {

    //~fields
    //==================================================================================================================
    /**
     * 路径撇配器
     */
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 应用ID为key，应用下的用户为VALUE，角色属性中不包含菜单信息
     */
    protected final Map<Long, Collection<UserInterface>> APP_USER_ROLE_MAP = new HashMap<>();

    /**
     * 应用ID为key，应用下所有角色为VALUE，角色属性中包含菜单信息
     */
    protected final Map<Long, Collection<? extends RoleInterface>> APP_ROLE_MENU_MAP = new HashMap<>();

    /**
     * 应用ID为key，应用下所有菜单为VALUE
     */
    protected final Map<Long, Collection<? extends MenuInterface>> APP_MENU_MAP = new HashMap<>();

    //~methods
    //==================================================================================================================

    /**
     * 获取应用配置的所有权限
     *
     * @param appId 应用ID，当参数{@code appId}值为null时，内部使用默认应用ID
     * @return 应用的所有菜单
     */
    @Override
    public Collection<? extends MenuInterface> getMenus(Long appId) {
        appId = Optional.ofNullable(appId).orElseGet(() -> GoudongAuthenticationClient.getDefaultClient().getAppId());
        Long finalAppId = appId;
        LogUtil.debug(log, () -> "查询应用{}所有菜单权限", () -> ArrayUtil.create(finalAppId));
        if (APP_MENU_MAP.containsKey(appId)) {
            LogUtil.debug(log, () -> "存在应用缓存，直接取出缓存");
            return APP_MENU_MAP.get(appId);
        }
        synchronized (this) {
            if (APP_MENU_MAP.containsKey(appId)) {
                LogUtil.debug(log, () -> "存在应用缓存，直接取出缓存");
                return APP_MENU_MAP.get(appId);
            }
            LogUtil.debug(log, () -> "应用缓存不存在，需要查询接口");
            GetMenusResp menusResp = PermissionV1Api.getMenus(GetMenusReq.builder().appId(appId).build()).getData();
            LogUtil.debug(log, () -> "查询应用{}所有菜单权限:{}", () -> ArrayUtil.create(finalAppId, JsonUtil.toJsonString(menusResp.getMenus())));
            APP_MENU_MAP.put(appId, menusResp.getMenus());
            return menusResp.getMenus();
        }
    }

    /**
     * 获取应用配置的所有角色和菜单
     *
     * @param appId 应用ID，当参数{@code appId}值为null时，内部使用默认应用ID
     * @return 应用的所有角色信息及角色对应的菜单
     */
    @Override
    public Collection<? extends RoleInterface> getRolesMenus(Long appId) {
        appId = Optional.ofNullable(appId).orElseGet(() -> GoudongAuthenticationClient.getDefaultClient().getAppId());
        Long finalAppId = appId;
        LogUtil.debug(log, () -> "查询应用{}所有角色及角色权限", () -> ArrayUtil.create(finalAppId));
        if (APP_ROLE_MENU_MAP.containsKey(appId)) {
            LogUtil.debug(log, () -> "存在应用缓存，直接取出缓存");
            return APP_ROLE_MENU_MAP.get(appId);
        }
        synchronized (this) {
            if (APP_ROLE_MENU_MAP.containsKey(appId)) {
                LogUtil.debug(log, () -> "存在应用缓存，直接取出缓存");
                return APP_ROLE_MENU_MAP.get(appId);
            }

            LogUtil.debug(log, () -> "应用缓存不存在，需要查询接口");
            GetRolesMenusResp rolesMenusResp = PermissionV1Api.getRolesMenus(GetRolesMenusReq.builder().appId(appId).build()).getData();
            LogUtil.debug(log, () -> "查询应用{}所有角色及角色权限:{}", () -> ArrayUtil.create(finalAppId, JsonUtil.toJsonString(rolesMenusResp.getRoles())));
            APP_ROLE_MENU_MAP.put(appId, rolesMenusResp.getRoles());
            return rolesMenusResp.getRoles();
        }
    }

    /**
     * 获取用户信息
     *
     * @param appId    应用ID，当参数{@code appId}值为null时，内部使用默认应用ID
     * @param username 用户名
     * @return 用户信息，包含用户基本信息和拥有的角色权限信息
     */
    @Override
    public UserInterface getUser(Long appId, String username) {
        appId = Optional.ofNullable(appId).orElseGet(() -> GoudongAuthenticationClient.getDefaultClient().getAppId());
        Long finalAppId = appId;
        LogUtil.debug(log, () -> "查询应用{}用户{}的信息", () -> ArrayUtil.create(finalAppId, username));
        // 先查询缓存是否命中
        if (APP_USER_ROLE_MAP.containsKey(appId)) {
            LogUtil.debug(log, () -> "存在应用缓存，直接取出缓存", () -> ArrayUtil.create(finalAppId));
            Optional<? extends UserInterface> first = APP_USER_ROLE_MAP.get(appId).stream().filter(f -> Objects.equals(username, f.getUsername())).findFirst();
            // 命中缓存，直接返回
            if (first.isPresent()) {
                return first.get();
            }
            LogUtil.debug(log, () -> "未查询到用户缓存，需要调用接口查询用户");
        }
        // 存缓存
        synchronized (this) {
            // 先查询缓存
            if (APP_USER_ROLE_MAP.containsKey(appId)) {
                LogUtil.debug(log, () -> "存在应用缓存，直接取出缓存", () -> ArrayUtil.create(finalAppId));
                Optional<? extends UserInterface> first = APP_USER_ROLE_MAP.get(appId).stream().filter(f -> Objects.equals(username, f.getUsername())).findFirst();
                if (first.isPresent()) {
                    return first.get();
                }
                LogUtil.debug(log, () -> "未查询到用户缓存，需要调用接口查询用户");
            }
            GetUserResp userResp = PermissionV1Api.getUser(GetUserReq.builder().appId(appId).username(username).build()).getData();
            LogUtil.debug(log, () -> "查询应用{}用户{}的信息:{}", () -> ArrayUtil.create(finalAppId, username, JsonUtil.toJsonString(userResp.getRoles())));

            // 存缓存
            if (APP_USER_ROLE_MAP.containsKey(appId)) {
                Collection<UserInterface> users = APP_USER_ROLE_MAP.get(appId);
                users.add(userResp);
            } else {
                Collection<UserInterface> users = new ArrayList<>();
                users.add(userResp);
                APP_USER_ROLE_MAP.put(appId, users);
            }

            return userResp;
        }
    }

    /**
     * 检查用户是否有访问资源的权限
     *
     * @param appId    用用ID，当参数{@code appId}值为null时，内部使用默认应用ID
     * @param username 用户名
     * @param resource 资源
     * @return true：有权限，false：无权限
     */
    @Override
    public boolean checkAccessRight(Long appId, String username, Resource resource) {
        appId = Optional.ofNullable(appId).orElseGet(() -> GoudongAuthenticationClient.getDefaultClient().getAppId());
        Long finalAppId = appId;
        LogUtil.debug(log, () -> "应用{}下的用户{},请求访问资源：{}", () -> ArrayUtil.create(finalAppId, username, JsonUtil.toJsonString(resource)));
        LogUtil.debug(log, () -> "查询所有菜单权限");
        Collection<? extends RoleInterface> roles = this.getRolesMenus(appId);
        // 本次资源对应的菜单
        List<Long> roleIds = new ArrayList<>();
        List<String> roleNames = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(roles)) {
            for(RoleInterface role : roles) {
                for (MenuInterface menu : role.getMenus()) {
                    String pathPattern = menu.getPath();
                    String method = menu.getMethod();
                    if (StringUtil.isNotBlank(pathPattern) && StringUtil.isNotBlank(method)) {
                        if (antPathMatcher.match(pathPattern, resource.getUrl()) && method.contains(resource.getMethod().name())) {
                            roleIds.add(role.getId());
                            roleNames.add(role.getName());
                            break;
                        }
                    }
                }
            }
        }

        if (CollectionUtil.isNotEmpty(roleIds)) {
            LogUtil.debug(log, () -> "该资源需要权限才能访问:{}", () -> ArrayUtil.create(roleNames));
            LogUtil.debug(log, () -> "开始判断用户：{} 是否拥有资源的访问权限", () -> ArrayUtil.create(username));
            UserInterface user = this.getUser(appId, username);
            boolean flag = user.getRoles().stream().anyMatch(f -> {
                if (roleIds.contains(f.getId())) {
                    LogUtil.debug(log, () -> "用户：{} 拥有角色 {}，允许访问资源 {}", () -> ArrayUtil.create(username, f.getName(), JsonUtil.toJsonString(resource)));
                    return true;
                }
                return false;
            });
            if (flag) {
                return true;
            }
            LogUtil.warn(log, () -> "用户：{}没有资源的访问权限", () -> ArrayUtil.create(username));
            return false;
        }

        LogUtil.debug(log, () -> "该资源不需要权限就能访问");
        return true;
    }

    /**
     * 清理缓存
     *
     * @param appId
     */
    @Override
    public void clean(Long appId) {
        APP_USER_ROLE_MAP.remove(appId);
        APP_ROLE_MENU_MAP.remove(appId);
        APP_MENU_MAP.remove(appId);
    }

}
