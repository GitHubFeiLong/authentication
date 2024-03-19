package com.goudong.authentication.client.core;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.Collection;
import java.util.Date;

/**
 * 类描述：
 * 角色基本信息
 * @author chenf
 */
@Data
public class BaseRole implements RoleInterface{
    //~fields
    //==================================================================================================================
    /**
     * 角色所在的应用ID
     */
    private Long appId;
    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色名称
     */
    private String name;

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
     * 菜单信息
     */
    @JsonDeserialize(contentAs = BaseMenu.class)
    private Collection<MenuInterface> menus;
    //~methods
    //==================================================================================================================
}
