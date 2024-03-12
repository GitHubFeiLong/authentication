package com.goudong.authentication.client.bean.permission;

import com.goudong.authentication.client.api.permission.v1.PermissionV1Api;
import com.goudong.authentication.client.api.permission.v1.req.PermissionListPermissionByUsernameReq;
import com.goudong.authentication.client.api.permission.v1.resp.PermissionListPermissionByUsername2SimpleResp;
import com.goudong.authentication.client.core.MenuInterface;
import com.goudong.authentication.client.core.PermissionInterface;
import com.goudong.authentication.client.core.Resource;
import com.goudong.authentication.client.core.RoleInterface;
import com.goudong.authentication.client.util.JsonUtil;
import com.goudong.authentication.client.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * 类描述：
 * 默认的权限校验器
 * @author cfl
 * @version 1.0
 */
@Slf4j
public class DefaultPermission implements PermissionInterface {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    /**
     * 获取用户的所有角色权限，不对结果缓存，每次都需要请求服务端接口获取结果。
     *
     * @param username 用户名
     * @return
     */
    @Override
    public Collection<? extends RoleInterface> getRoles(String username) {
        if (log.isDebugEnabled()) {
            log.debug("获取用户：{}的权限", username);
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
        if (log.isDebugEnabled()) {
            log.debug("判断用户：{} 是否拥有资源：{}的访问权限", username, JsonUtil.toJsonString(resource));
        }
        Collection<? extends RoleInterface> roles = this.getRoles(username);
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (RoleInterface role : roles) {
            for(MenuInterface menu : role.getMenus()) {
                String pathPattern = menu.getPath();
                String method = menu.getMethod();
                if (StringUtil.isNotBlank(pathPattern) && StringUtil.isNotBlank(method)) {
                    if (antPathMatcher.match(pathPattern, resource.getUrl()) && method.contains(resource.getMethod().name())) {
                        if (log.isDebugEnabled()) {
                            log.debug("用户：{} 拥有资源：{}的访问权限", username, JsonUtil.toJsonString(resource));
                        }
                        return true;
                    }
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("用户：{} 不拥有资源：{}的访问权限", username, JsonUtil.toJsonString(resource));
        }
        return false;
    }
}
