package com.goudong.authentication.client.core;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * 类描述：
 * 菜单基本信息
 * @author chenf
 */
@Data
public class BaseMenu implements MenuInterface, TreeInterface<Long, Long, MenuInterface>, Comparable<MenuInterface>, Serializable {
    //~fields
    //==================================================================================================================
    private static final long serialVersionUID = -1924856851948151836L;

    /**
     * 菜单id
     */
    private Long id;

    /**
     * 父级主键id
     */
    private Long parentId;

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 权限标识
     */
    private String permissionId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单类型（1：菜单；2：按钮；3：接口）
     */
    private Integer type;

    /**
     * 路由或接口地址
     */
    private String path;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 排序字段（值越小越靠前，仅仅针对前端路由）
     */
    private Integer sortNum;

    /**
     * 是否是隐藏菜单
     */
    private Boolean hide;

    /**
     * 前端菜单元数据
     */
    private String meta;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createdDate;

    /**
     * 最后修改时间
     */
    private Date lastModifiedDate;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 最后修改人
     */
    private String lastModifiedBy;

    /**
     * 子菜单
     */
    @JsonDeserialize(contentAs = BaseMenu.class)
    private Collection<MenuInterface> children;

    @Override
    @JsonDeserialize(contentAs = BaseMenu.class)
    public void setChildren(Collection<MenuInterface> children) {
        this.children = children;
    }

    @Override
    public int compareTo(MenuInterface o) {
        return this.getSortNum().compareTo(o.getSortNum());
    }

    //~methods
    //==================================================================================================================
}
