package com.goudong.authentication.server.lang;

import com.goudong.authentication.server.util.PageTypeEnum;
import lombok.Setter;
import lombok.ToString;

/**
 * 类描述：
 * 分页对象
 * @Author msi
 * @Date 2021-08-19 21:37
 * @Version 1.0
 */
@Setter
@ToString
public class BasePage {

    // @NotNull(message = "分页查询page参数必传")
    // @Min(value = 1, message = "分页参数错误，page必须大于等于1")
    // @ApiModelProperty(value = "第几页,从1开始",required = true)
    private Integer page = 1;

    // @NotNull(message = "分页查询size参数必传")
    // @Min(value = 1, message = "分页参数错误，size必须大于等于1")
    // @ApiModelProperty(value = "一页显示内容长度", required = true)
    private Integer size = 20;

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }

    /**
     * 获取spring-data-jpa 框架的页码（以0开始），
     * @return
     */
    // @ApiModelProperty(hidden = true)
    public Integer getJPAPage() {
        return page == null ? null : PageTypeEnum.JPA.getPage(page);
    }



}
