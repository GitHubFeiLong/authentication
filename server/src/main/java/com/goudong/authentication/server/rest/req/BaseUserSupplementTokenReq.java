package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 类描述：
 * 补充token信息
 * @author chenf
 */
@Data
public class BaseUserSupplementTokenReq {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "用户令牌", required = true)
    @NotBlank
    private String token;

    @ApiModelProperty(value = "待添加的参数", required = true)
    @NotNull
    @NotEmpty
    private Map<String, Object> detail;

    //~methods
    //==================================================================================================================
}
