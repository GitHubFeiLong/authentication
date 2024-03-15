package com.goudong.authentication.client.api.user.v1.req;

import com.goudong.authentication.client.core.BaseApiReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 类描述：
 * 删除用户
 * @author chenf
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseUserDeleteByIdsReq extends BaseApiReq implements Serializable {

    private static final long serialVersionUID = -8603583888249556831L;

    /**
     * 用户id集合
     */
    private Collection<Long> ids;
}
