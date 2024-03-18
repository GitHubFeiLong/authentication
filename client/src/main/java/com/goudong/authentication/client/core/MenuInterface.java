package com.goudong.authentication.client.core;

/**
 * 类描述：
 * 菜单接口，定义菜单基本方法
 * @author chenf
 */
public interface MenuInterface {
    /**
     * 获取菜单ID
     * @return 菜单ID
     */
    Long getId();

    /**
     * 设置菜单ID
     * @param id 菜单ID
     */
    void setId(Long id);

    /**
     * 获取父级主键id
     * @return 父菜单ID
     */
    Long getParentId();

    /**
     * 设置父级主键ID
     * @param parentId  父菜单ID
     */
    void setParentId(Long parentId);

    /**
     * 获取应用ID
     * @return 应用ID
     */
    Long getAppId();

    /**
     * 设置应用ID
     * @param appId 应用ID
     */
    void setAppId(Long appId);

    /**
     * 获取权限标识
     * @return 权限标识
     */
    String getPermissionId();

    /**
     * 设置权限标识
     * @param permissionId  权限标识
     */
    void setPermissionId(String permissionId);

    /**
     * 获取菜单名称
     * @return 菜单名称
     */
    String getName();

    /**
     * 设置菜单名称
     * @param name  菜单名称
     */
    void setName(String name);

    /**
     * 菜单类型（1：菜单；2：按钮；3：接口）
     * @return 菜单类型
     */
    Integer getType();

    /**
     * 设置菜单类型
     * @param type 1：菜单；2：按钮；3：接口
     */
    void setType(Integer type);

    /**
     * 获取路由或接口地址
     * @return 获取地址
     */
    String getPath();

    /**
     * 菜单路由或接口地址
     * @param path  路由或接口地址
     */
    void setPath(String path);

    /**
     * 请求方式，JSON数组，格式类似是：["GET","POST"]
     * @return 请求方式
     */
    String getMethod();

    /**
     * 设置菜单请求方式
     * @param method 请求方式，值是JSON数组，格式类似是：["GET","POST"]
     */
    void setMethod(String method);

    /**
     * 获取菜单排序值
     * @return 排序
     */
    Integer getSortNum();

    /**
     * 设置菜单排序编号
     * @param sortNum 排序编号
     */
    void setSortNum(Integer sortNum);

    /**
     * 获取是否隐藏
     * @return true：隐藏菜单；false：显示菜单
     */
    Boolean getHide();

    /**
     * 设置是否隐藏
     * @param hide true：隐藏菜单；false：显示菜单
     */
    void setHide(Boolean hide);

    /**
     * 获取菜单元数据
     * @return 菜单元数据
     */
    String getMeta();

    /**
     * 设置菜单元素据
     * @param meta 菜单元素据，json字符串
     */
    void setMeta(String meta);
}
