package com.goudong.authentication.client.api.user.v1.req;

import com.goudong.authentication.client.core.BaseApiReq;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * 新增用户
 * @author chenf
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseUserSimpleCreateReq extends BaseApiReq implements Serializable {

    private static final long serialVersionUID = -7466849523525350832L;
    /**
     * 用户名（16）
     */
    private String username;

    /**
     * 密码{@code password == null || password.length() == 0} 时，使用系统设置的默认密码
     */
    private String password;

    /**
     * 角色id集合
     */
    private List<Long> roleIds;

    /**
     * 备注
     */
    private String remark;
}
