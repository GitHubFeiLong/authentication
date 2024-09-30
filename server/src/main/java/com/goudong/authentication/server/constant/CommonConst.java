package com.goudong.authentication.server.constant;


import com.goudong.authentication.common.util.ListUtil;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 类描述：
 * 常量类
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
            1900L
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
            "/**/druid/**",
            "/**/actuator/**"
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

    /**
     * BCrypt格式的字符串
     * {@code BCryptPasswordEncoder#BCRYPT_PATTERN}
     */
    public static final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");


    /**
     * 认证模式 GOUDONG-SHA256withRSA
     */
    public static final String AUTHORIZATION_GOUDONG_MODEL = "GOUDONG-SHA256withRSA";

    /**
     * application/json
     */
    public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    //~methods
    //==================================================================================================================
}
