package com.goudong.authentication.client.core;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.Collection;
import java.util.Date;

/**
 * 类描述：
 *
 * @author chenf
 */
@Data
public class BaseUser implements UserInterface{
    //~fields
    //==================================================================================================================
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 应用id
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
     * 密码
     */
    private String password;

    /**
     * 激活状态：true 激活；false 未激活
     */
    private Boolean enabled;

    /**
     * 锁定状态：true 锁定；false 未锁定
     */
    private Boolean locked;

    /**
     * 有效截止时间
     */
    private Date validTime;

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
     * 角色
     */
    @JsonDeserialize(contentAs = BaseRole.class)
    private Collection<RoleInterface> roles;

    //~methods
    //==================================================================================================================
}
