package com.goudong.authentication.server.service.dto;

import com.goudong.authentication.common.constant.CommonConst;
import com.goudong.authentication.common.core.UserSimple;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 类描述：
 * 认证对象
 * @author chenf
 */
@Data
public class MyAuthentication implements Authentication {

    private static final long serialVersionUID = 2854631889050732676L;
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户所在应用id
     */
    private Long appId;

    /**
     * 真实应用id（例如xx应用管理员，app_id是认证服务应用的app_id，但是real_app_id是自己所管理xx应用的app_id）
     */
    private Long realAppId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 角色
     */
    private List<SimpleGrantedAuthority> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * Stores additional details about the authentication request. These might be an IP
     * address, certificate serial number etc.
     *
     * @return additional details about the authentication request, or <code>null</code>
     * if not used
     */
    @Override
    public Object getDetails() {
        return null;
    }


    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    /**
     * Returns the name of this principal.
     *
     * @return the name of this principal.
     */
    @Override
    public String getName() {
        return this.username;
    }

    /**
     * 是否是超级管理员
     * @return true 超级管理员，false 不是超级管理员
     */
    public boolean superAdmin() {
        return this.roles.stream().anyMatch(f -> Objects.equals(f.getAuthority(), CommonConst.ROLE_APP_SUPER_ADMIN));
    }

    /**
     * 判断登录用户，是否是管理员或超级管理员
     * @return true 管理员，false 普通用户
     */
    public boolean admin() {
        return this.roles.stream().anyMatch(f -> Objects.equals(f.getAuthority(), CommonConst.ROLE_APP_ADMIN) || Objects.equals(f.getAuthority(), CommonConst.ROLE_APP_SUPER_ADMIN));
    }

    /**
     * 转换成{@link UserSimple}对象
     * @return UserSimple对象
     */
    public UserSimple convertUserSimple() {
        UserSimple userSimple = new UserSimple();
        userSimple.setId(this.getId());
        userSimple.setAppId(this.getAppId());
        userSimple.setRealAppId(this.getRealAppId());
        userSimple.setUsername(this.getUsername());
        userSimple.setRoles(this.getRoles().stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList()));

        return userSimple;
    }
}
