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
     * 应用下的所有菜单
     */
    public static final String APP_MENUS = "goudong:app:menus:{appId}";

    /**
     * 应用下的所有角色
     */
    public static final String APP_ROLES = "goudong:app:roles:{appId}";

    /**
     * 应用下的用户
     */
    public static final String APP_USERS = "goudong:app:users:{appId}:{username}";

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
