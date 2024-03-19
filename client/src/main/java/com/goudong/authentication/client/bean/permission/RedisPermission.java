package com.goudong.authentication.client.bean.permission;

import com.goudong.authentication.client.api.permission.v1.PermissionV1Api;
import com.goudong.authentication.client.api.permission.v1.req.GetMenusReq;
import com.goudong.authentication.client.api.permission.v1.req.GetRolesMenusReq;
import com.goudong.authentication.client.api.permission.v1.req.GetUserReq;
import com.goudong.authentication.client.api.permission.v1.resp.GetMenusResp;
import com.goudong.authentication.client.api.permission.v1.resp.GetRolesMenusResp;
import com.goudong.authentication.client.api.permission.v1.resp.GetUserResp;
import com.goudong.authentication.client.constant.RedisKeyConst;
import com.goudong.authentication.client.core.MenuInterface;
import com.goudong.authentication.client.core.Resource;
import com.goudong.authentication.client.core.RoleInterface;
import com.goudong.authentication.client.core.UserInterface;
import com.goudong.authentication.client.dto.BaseMenuDTO;
import com.goudong.authentication.client.dto.BaseRoleDTO;
import com.goudong.authentication.client.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    public Collection<MenuInterface> getMenus(Long appId) {
        redisTemplate.getConnectionFactory().getConnection().info("xxx");

        appId = Optional.ofNullable(appId).orElseGet(() -> GoudongAuthenticationClient.getDefaultClient().getAppId());
        Long finalAppId = appId;
        LogUtil.debug(log, () -> "查询应用{}所有菜单权限", () -> ArrayUtil.create(finalAppId));
        String key = RedisKeyConst.getKey(RedisKeyConst.APP_MENUS, appId);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            LogUtil.debug(log, () -> "存在应用缓存，直接取出缓存");
            List range = redisTemplate.opsForList().range(key, 0, -1);
            return range;
        }
        synchronized (this) {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
                LogUtil.debug(log, () -> "存在应用缓存，直接取出缓存");
                List range = redisTemplate.opsForList().range(key, 0, -1);
                return range;
            }
            LogUtil.debug(log, () -> "应用缓存不存在，需要查询接口");
            GetMenusResp menusResp = PermissionV1Api.getMenus(GetMenusReq.builder().appId(appId).build()).getData();
            Collection<MenuInterface> menus = menusResp.getMenus();
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
    public Collection<RoleInterface> getRolesMenus(Long appId) {
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
            Collection<RoleInterface> roles = rolesMenusResp.getRoles();

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
     * @return 用户信息，包含用户基本信息和拥有的角色信息
     */
    @Override
    public UserInterface getUser(Long appId, String username) {
        appId = Optional.ofNullable(appId).orElseGet(() -> GoudongAuthenticationClient.getDefaultClient().getAppId());
        Long finalAppId = appId;
        LogUtil.debug(log, () -> "查询应用{}用户{}的信息", () -> ArrayUtil.create(finalAppId, username));
        String key = RedisKeyConst.getKey(RedisKeyConst.APP_USERS, appId, username);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            return JsonUtil.toObject(redisTemplate.opsForValue().get(key).toString(), GetUserResp.class);
        }
        synchronized (this) {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
                return JsonUtil.toObject(redisTemplate.opsForValue().get(key).toString(), GetUserResp.class);
            }
            LogUtil.debug(log, () -> "应用缓存不存在，需要查询接口");
            GetUserResp userResp = PermissionV1Api.getUser(GetUserReq.builder().appId(appId).username(username).build()).getData();
            LogUtil.debug(log, () -> "查询应用{}用户{}的信息:{}", () -> ArrayUtil.create(finalAppId, username, JsonUtil.toJsonString(userResp)));

            // 设置缓存
            redisTemplate.opsForValue().set(key, JsonUtil.toJsonString(userResp), 8, TimeUnit.HOURS);
            return userResp;
        }

    }

    /**
     * 获取用户信息（包含菜单权限）
     *
     * @param appId    应用ID，当参数{@code appId}值为null时，内部使用默认应用ID
     * @param username 用户名
     * @return 用户信息，包含用户基本信息和拥有的角色权限信息
     */
    @Override
    public UserInterface getUserDetail(Long appId, String username) {
        appId = Optional.ofNullable(appId).orElseGet(() -> GoudongAuthenticationClient.getDefaultClient().getAppId());
        Long finalAppId = appId;
        LogUtil.debug(log, () -> "查询应用{}用户{}的详细信息", () -> ArrayUtil.create(finalAppId, username));
        UserInterface userResp = this.getUser(appId, username);
        if (CollectionUtil.isNotEmpty(userResp.getRoles())) {
            LogUtil.debug(log, () -> "查询用户角色的权限信息");
            Collection<RoleInterface> rolesMenus = this.getRolesMenus(appId);
            Map<Long, RoleInterface> roleIdRolemap = rolesMenus.stream().collect(Collectors.toMap(RoleInterface::getId, p -> p, (k1, k2) -> k1));
            userResp.getRoles().forEach(p -> {
                if (roleIdRolemap.containsKey(p.getId())) {
                    Collection<MenuInterface> menus = roleIdRolemap.get(p.getId()).getMenus();
                    p.setMenus(menus);
                }
            });
        }
        return userResp;
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
     * @param appId 应用id
     */
    @Override
    public void clean(Long appId) {
        appId = Optional.ofNullable(appId).orElseGet(() -> GoudongAuthenticationClient.getDefaultClient().getAppId());
        Long finalAppId = appId;
        LogUtil.debug(log, () -> "查询应用{}的所有缓存key", () -> ArrayUtil.create(finalAppId));
        List<String> keys = ListUtil.newArrayList(
                RedisKeyConst.getKey(RedisKeyConst.APP_MENUS, appId),
                RedisKeyConst.getKey(RedisKeyConst.APP_ROLES, appId)
        );
        List<String> keyPatterns = ListUtil.newArrayList(
                RedisKeyConst.getKey(RedisKeyConst.APP_USERS, appId, "*")
        );
        keyPatterns.forEach(pattern -> {
            keys.addAll(getKeysByScan(pattern));
        });

        LogUtil.debug(log, () -> "删除应用的缓存成功");
        redisTemplate.delete(keys);
    }

    /**
     * 获取符合模式的缓存key
     * @param patten        模式
     * @return  符合模式的所有key
     */
    private Set<String> getKeysByScan(String patten) {
        return (Set<String>) redisTemplate.execute(connect -> {
            Set<String> binaryKeys = new HashSet<>();
            Cursor<byte[]> cursor = connect.scan(new ScanOptions.ScanOptionsBuilder().match(patten).count(200000).build());
            while (cursor.hasNext() && binaryKeys.size() < 200000) {
                binaryKeys.add(new String(cursor.next()));
            }
            return binaryKeys;
        }, true);
    }
}
