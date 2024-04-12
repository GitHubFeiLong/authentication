package com.goudong.authentication.server.easyexcel.template;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：
 * 字典明细导出
 * @author chenf
 */
@Data
public class BaseDictExportTemplate {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("序号")
    @ExcelProperty("序号")
    @ColumnWidth(6)
    private Integer sequenceNumber;

    @ApiModelProperty("字典编码")
    @ExcelProperty("字典编码")
    @ColumnWidth(20)
    private String code;

    @ApiModelProperty("字典名称")
    @ExcelProperty("字典名称")
    @ColumnWidth(20)
    private String name;

    @ApiModelProperty("配置数量")
    @ExcelProperty("配置数量")
    @ColumnWidth(20)
    private Integer dictSettingNumber;

    @ApiModelProperty("字典模板")
    @ExcelProperty("字典模板")
    @ColumnWidth(20)
    private String template;

    @ApiModelProperty("字典状态")
    @ExcelProperty("字典状态")
    @ColumnWidth(20)
    private String enableStatus;

    @ApiModelProperty("字典备注")
    @ExcelProperty("字典备注")
    @ColumnWidth(40)
    private String remark;

    @ApiModelProperty("创建时间")
    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    private Date createdDate;
    //~methods
    //==================================================================================================================
}
