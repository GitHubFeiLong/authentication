package com.goudong.authentication.client.bean.permission;

import com.goudong.authentication.client.api.permission.v1.PermissionV1Api;
import com.goudong.authentication.client.api.permission.v1.req.GetMenusReq;
import com.goudong.authentication.client.api.permission.v1.req.GetRolesMenusReq;
import com.goudong.authentication.client.api.permission.v1.req.PermissionListPermissionByUsernameReq;
import com.goudong.authentication.client.api.permission.v1.resp.GetMenusResp;
import com.goudong.authentication.client.api.permission.v1.resp.GetRolesMenusResp;
import com.goudong.authentication.client.api.permission.v1.resp.PermissionListPermissionByUsername2SimpleResp;
import com.goudong.authentication.client.constant.RedisKeyConst;
import com.goudong.authentication.client.bean.permission.PermissionInterface;
import com.goudong.authentication.client.core.MenuInterface;
import com.goudong.authentication.client.core.Resource;
import com.goudong.authentication.client.core.RoleInterface;
import com.goudong.authentication.client.core.UserInterface;
import com.goudong.authentication.client.dto.BaseMenuDTO;
import com.goudong.authentication.client.dto.BaseRoleDTO;
import com.goudong.authentication.client.util.ArrayUtil;
import com.goudong.authentication.client.util.GoudongAuthenticationClient;
import com.goudong.authentication.client.util.JsonUtil;
import com.goudong.authentication.client.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * 权限校验器，使用redis作为缓存，提高效率
 * @author cfl
 * @version 1.0
 */
@Slf4j
@Component
public class RedisPermission implements PermissionInterface {

    //~fields
    //==================================================================================================================
    /**
     * redis模板
     */
    private final RedisTemplate redisTemplate;

    /**
     * 路径撇配器
     */
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    //~methods
    //==================================================================================================================
    public RedisPermission(RedisTemplate redisTemplate) {
        LogUtil.debug(log, () -> "初始化RedisPermission");
        this.redisTemplate = redisTemplate;
    }


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
        String key = RedisKeyConst.getKey(RedisKeyConst.APP_MENUS, appId);
        if (redisTemplate.hasKey(key)) {
            LogUtil.debug(log, () -> "存在应用缓存，直接取出缓存");
            List range = redisTemplate.opsForList().range(key, 0, -1);
            return range;
        }
        synchronized (this) {
            if (redisTemplate.hasKey(key)) {
                LogUtil.debug(log, () -> "存在应用缓存，直接取出缓存");
                List range = redisTemplate.opsForList().range(key, 0, -1);
                return range;
            }
            LogUtil.debug(log, () -> "应用缓存不存在，需要查询接口");
            GetMenusResp menusResp = PermissionV1Api.getMenus(GetMenusReq.builder().appId(appId).build()).getData();
            List<BaseMenuDTO> menus = menusResp.getMenus();
            LogUtil.debug(log, () -> "查询应用{}所有菜单权限:{}", () -> ArrayUtil.create(finalAppId, JsonUtil.toJsonString(menus)));

            redisTemplate.execute(new SessionCallback() {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException {
                    operations.multi();
                    // 设置缓存
                    redisTemplate.opsForList().rightPushAll(key, menus);
                    // 设置失效
                    redisTemplate.expire(key, 1, TimeUnit.DAYS);
                    // 执行
                    return operations.exec();
                }
            });
            return menus;
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
        String key = RedisKeyConst.getKey(RedisKeyConst.APP_ROLES, appId);
        if (redisTemplate.hasKey(key)) {
            LogUtil.debug(log, () -> "存在应用缓存，直接取出缓存");
            List range = redisTemplate.opsForList().range(key, 0, -1);
            return range;
        }

        synchronized (this) {
            if (redisTemplate.hasKey(key)) {
                LogUtil.debug(log, () -> "存在应用缓存，直接取出缓存");
                List range = redisTemplate.opsForList().range(key, 0, -1);
                return range;
            }

            LogUtil.debug(log, () -> "应用缓存不存在，需要查询接口");
            GetRolesMenusResp rolesMenusResp = PermissionV1Api.getRolesMenus(GetRolesMenusReq.builder().appId(appId).build()).getData();
            List<BaseRoleDTO> roles = rolesMenusResp.getRoles();
            LogUtil.debug(log, () -> "查询应用{}所有角色及角色权限:{}", () -> ArrayUtil.create(finalAppId, JsonUtil.toJsonString(roles)));
            redisTemplate.execute(new SessionCallback() {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException {
                    operations.multi();
                    // 设置缓存
                    redisTemplate.opsForList().rightPushAll(key, roles);
                    // 设置失效
                    redisTemplate.expire(key, 1, TimeUnit.DAYS);
                    // 执行
                    return operations.exec();
                }
            });

            return roles;
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
        if () {

        }
        return null;
    }

    /**
     * 检查用户是否有访问资源的权限
     *
     * @param appId    应用ID，当参数{@code appId}值为null时，内部使用默认应用ID
     * @param username 用户名
     * @param resource 资源
     * @return true：有权限，false：无权限
     */
    @Override
    public boolean checkAccessRight(Long appId, String username, Resource resource) {
        return false;
    }

    /**
     * 清理缓存
     *
     * @param appId
     */
    @Override
    public void clean(Long appId) {

    }
}
