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
     * 获取父级主键id
     * @return 父菜单ID
     */
    Long getParentId();

    /**
     * 获取应用ID
     * @return 应用ID
     */
    Long getAppId();

    /**
     * 获取权限标识
     * @return 权限标识
     */
    String getPermissionId();

    /**
     * 获取菜单名称
     * @return 菜单名称
     */
    String getName();

    /**
     * 菜单类型（1：菜单；2：按钮；3：接口）
     * @return 菜单类型
     */
    Integer getType();

    /**
     * 获取路由或接口地址
     * @return 获取地址
     */
    String getPath();

    /**
     * 请求方式，JSON数组，格式类似是：["GET","POST"]
     * @return 请求方式
     */
    String getMethod();

    /**
     * 获取菜单排序值
     * @return 排序
     */
    Integer getSortNum();

    /**
     * 获取是否隐藏
     * @return true：隐藏菜单；false：显示菜单
     */
    Boolean getHide();

    /**
     * 获取菜单元数据
     * @return 菜单元数据
     */
    String getMeta();
}
