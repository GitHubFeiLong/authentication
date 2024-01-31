package com.goudong.authentication.server.constant;

import com.goudong.core.util.ListUtil;

import java.util.List;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 */
public class CommonConst {
    //~fields
    //==================================================================================================================
    /**
     * 认证服务appId
     */
    public static final Long AUTHENTICATION_SERVER_APP_ID = 1L;
    /**
     * 超级管理员角色
     */
    public static final String ROLE_APP_SUPER_ADMIN = "ROLE_APP_SUPER_ADMIN";

    /**
     * 管理员角色
     */
    public static final String ROLE_APP_ADMIN = "ROLE_APP_ADMIN";

    /**
     * 排除ROLE_APP_ADMIN 角色不能操作的菜单权限
     */
    public static final List<Long> ROLE_APP_ADMIN_IGNORE_MENUS = ListUtil.newArrayList(
            // 应用管理
            1400L
    );

    /**
     * http 请求方式
     */
    public static final String[] HTTP_METHODS = new String[]{"GET", "POST", "PUT", "DELETE", "HEAD", "PATCH", "OPTIONS", "TRACE"};

    /**
     * 逗号
     */
    public static final String COMMA = ",";

    /**
     * 静态资源
     */
    public static final List<String> STATIC_URIS = ListUtil.newArrayList(
            "/**/*.html*",
            "/**/*.css*",
            "/**/*.js*",
            "/**/*.ico*",
            "/**/swagger-resources*",
            "/**/api-docs*",
            "/druid/**",
            "/actuator/**"
    );

    /**
     * 白名单接口
     */
    public static final List<String> IGNORE_URIS = ListUtil.newArrayList(
            "/error?*",
            "/user/login?*",
            "/**/base-user/info/*",
            "/drop-down/base-app/all-drop-down" // 应用下拉
    );


    //~methods
    //==================================================================================================================
}
