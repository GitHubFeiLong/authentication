package com.goudong.authentication.server.easyexcel.template;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：
 * 应用导出
 * @author chenf
 */
@Data
public class BaseAppExportTemplate {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("序号")
    @ExcelProperty("序号")
    @ColumnWidth(6)
    private Integer sequenceNumber;

    @ApiModelProperty("应用id")
    @ExcelProperty("应用id")
    @ColumnWidth(20)
    private Long id;

    @ApiModelProperty("应用密钥")
    @ExcelProperty("应用密钥")
    @ColumnWidth(40)
    private String secret;

    @ApiModelProperty("应用名称")
    @ExcelProperty("应用名称")
    @ColumnWidth(15)
    private String name;

    @ApiModelProperty("登录回调页面，处理登录逻辑")
    @ExcelProperty("应用回调地址")
    @ColumnWidth(20)
    private String homePage;

    @ApiModelProperty("应用状态")
    @ExcelProperty("应用状态")
    @ColumnWidth(20)
    private String enableStatus;

    @ApiModelProperty("应用备注")
    @ExcelProperty("应用备注")
    @ColumnWidth(20)
    private String remark;

    @ApiModelProperty("创建时间")
    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    private Date createdDate;
    //~methods
    //==================================================================================================================
}
