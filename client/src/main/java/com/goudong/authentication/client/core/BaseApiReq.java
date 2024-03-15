package com.goudong.authentication.client.core;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 类描述：
 *
 * @author chenf
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class BaseApiReq {
    //~fields
    //==================================================================================================================
    /**
     * 应用id，请求参数携带该参数时，将获取该应用的信息生成令牌
     */
    protected Long appId;

    //~methods
    //==================================================================================================================
}
