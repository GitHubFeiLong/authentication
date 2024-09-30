package com.goudong.authentication.server.config.security;

import com.goudong.authentication.common.constant.CommonConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 类描述：
 * 权限校验
 * @author chenf
 */
@Slf4j
@Component
public class AccessDecisionManagerImpl implements AccessDecisionManager {

    /**
     * 权限校验
     * @param authentication    待校验的用户
     * @param o                 请求对象
     * @param collection        资源需要的权限集合{@link FilterInvocationSecurityMetadataSourceImpl#getAttributes(Object)} 返回值
     */
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) {
        // 当前请求需要的权限
        log.debug("本次请求需要权限:{}", collection);
        // 当前用户所具有的权限
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        log.debug("用户:{} 权限:{}", authentication.getPrincipal().toString(), authorities);

        for (ConfigAttribute configAttribute : collection) {
            // 当前请求需要的权限
            String needRole = configAttribute.getAttribute();
            // 包含其中一个角色即可访问
            if (authorities.stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(needRole))) {
                return;
            }

            if (CommonConst.ROLE_ANONYMOUS.equals(needRole)) {
                return;
            }
        }
        // 无权访问该路径
        throw new AccessDeniedException("SimpleGrantedAuthority!!");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
