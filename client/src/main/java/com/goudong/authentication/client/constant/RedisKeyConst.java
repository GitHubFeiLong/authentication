package com.goudong.authentication.client.constant;

/**
 * 类描述：
 * redis的key常量
 * @author cfl
 * @version 1.0
 */
public class RedisKeyConst {
    //~fields
    //==================================================================================================================
    /**
     * 正则表达式.
     */
    private static final String PATTERN = "\\{.*\\}";

    /**
     * 用户角色
     */
    public static final String USER_ROLE = "goudong:user:role:{username}";

    /**
     * 角色菜单权限
     */
    public static final String ROLE_MENU = "goudong:role:menu:{roleName}";

    //~methods
    //==================================================================================================================

    /**
     * 获取完整的key
     * @param keyPattern    key模式
     * @param param         参数
     * @return  完整的缓存键
     */
    public static String getKey(String keyPattern, Object... param) {
        if (param == null || param.length == 0) {
            return keyPattern;
        }
        for (int i = 0; i < param.length; i++) {
            keyPattern = keyPattern.replaceFirst(PATTERN, String.valueOf(param[i]));
        }
        return keyPattern;
    }
}
