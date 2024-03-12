package com.goudong.authentication.client.bean.permission;

import com.goudong.authentication.client.api.permission.v1.PermissionV1Api;
import com.goudong.authentication.client.api.permission.v1.req.PermissionListPermissionByUsernameReq;
import com.goudong.authentication.client.api.permission.v1.resp.PermissionListPermissionByUsername2SimpleResp;
import com.goudong.authentication.client.constant.RedisKeyConst;
import com.goudong.authentication.client.core.PermissionInterface;
import com.goudong.authentication.client.core.Resource;
import com.goudong.authentication.client.core.RoleInterface;
import com.goudong.authentication.client.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

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
    private final StringRedisTemplate stringRedisTemplate;
    //~methods
    //==================================================================================================================
    public RedisPermission(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    /**
     * 获取用户的所有角色权限
     *
     * @param username 用户名
     * @return
     */
    @Override
    public Collection<? extends RoleInterface> getRoles(String username) {
        if (log.isDebugEnabled()) {
            log.debug("获取用户：{}的权限", username);
        }
        String key = RedisKeyConst.getKey(RedisKeyConst.USER_ROLE, username);
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            if (log.isDebugEnabled()) {
                log.debug("用户{}存在角色缓存，从缓冲中取出角色信息", username);
            }
            String rolesValue = stringRedisTemplate.opsForValue().get(key);
            List<String> roleNames = JsonUtil.toList(rolesValue, String.class);

            roleNames.forEach(roleName -> {
                if (log.isDebugEnabled()) {
                    log.debug("查询角色：{}拥有的权限", roleName);
                }
                String key = RedisKeyConst.getKey(RedisKeyConst.USER_ROLE)
                if (Boolean.TRUE.equals(stringRedisTemplate.hasKey())) {

                }
            });
        }
        List<PermissionListPermissionByUsername2SimpleResp.RoleInner> roles = PermissionV1Api.listPermissionByUsername2Simple(PermissionListPermissionByUsernameReq.builder().username(username).build()).getData().getRoles();
        if (log.isDebugEnabled()) {
            log.debug("获取到用户：{}的权限列表：{}", JsonUtil.toJsonString(roles));
        }
        return roles;
    }

    /**
     * 判断用户是否有访问资源的权限
     *
     * @param username 用户名
     * @param resource 资源
     * @return true：有权限，false：无权限
     */
    @Override
    public boolean hasPermission(String username, Resource resource) {
        return false;
    }
}
