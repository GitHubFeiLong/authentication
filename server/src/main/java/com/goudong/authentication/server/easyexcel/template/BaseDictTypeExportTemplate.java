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
public class BaseDictTypeExportTemplate {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("序号")
    @ExcelProperty("序号")
    @ColumnWidth(6)
    private Integer sequenceNumber;

    @ApiModelProperty("类型编码")
    @ExcelProperty("类型编码")
    @ColumnWidth(20)
    private String code;

    @ApiModelProperty("类型名称")
    @ExcelProperty("类型名称")
    @ColumnWidth(20)
    private String name;

    @ApiModelProperty("字典数量")
    @ExcelProperty("字典数量")
    @ColumnWidth(20)
    private Integer dictNumber;

    @ApiModelProperty("类型模板")
    @ExcelProperty("类型模板")
    @ColumnWidth(20)
    private String template;

    @ApiModelProperty("类型状态")
    @ExcelProperty("类型状态")
    @ColumnWidth(20)
    private String enableStatus;

    @ApiModelProperty("类型备注")
    @ExcelProperty("类型备注")
    @ColumnWidth(40)
    private String remark;

    @ApiModelProperty("创建时间")
    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    private Date createdDate;
    //~methods
    //==================================================================================================================
}
