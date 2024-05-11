package com.goudong.authentication.server.rest.req;

import com.goudong.authentication.server.rest.req.search.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 类描述：
 * 分页字典配置
 * @author chenf
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseDictSettingPageReq extends BasePage {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "字典配置id集合", hidden = true)
    private List<Long> ids;

    @ApiModelProperty(value = "字典类型主键")
    private Long dictTypeId;

    @ApiModelProperty(value = "字典主键")
    private Long dictId;

    @ApiModelProperty("字典配置名称")
    private String name;

    //~methods
    //==================================================================================================================
}
