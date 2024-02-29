package com.goudong.authentication.client.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 类描述：
 * 用户实体对象
 * @author chenf
 */
@Data
public class BaseUserDTO implements Serializable {

    private static final long serialVersionUID = 1287763063397498182L;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 用户名（16位）
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
}
