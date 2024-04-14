package com.goudong.authentication.server.easyexcel.template;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * excel导入字典明细
 * @author chenf
 */
@Data
public class BaseDictImportExcelTemplate {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("类型编码")
    @ExcelProperty("* 类型编码")
    @ColumnWidth(20)
    private String dictTypeCode;

    @ApiModelProperty("字典编码")
    @ExcelProperty("* 字典编码")
    @ColumnWidth(20)
    private String code;

    @ApiModelProperty("字典名称")
    @ExcelProperty("* 字典名称")
    @ColumnWidth(20)
    private String name;

    @ApiModelProperty("配置模板")
    @ExcelProperty("配置模板")
    @ColumnWidth(20)
    private String template;

    @ApiModelProperty("类型状态")
    @ExcelProperty("* 激活状态")
    @ColumnWidth(20)
    private String enabled;

    @ApiModelProperty("备注")
    @ExcelProperty("备注")
    @ColumnWidth(40)
    private String remark;
    //~methods
    //==================================================================================================================
}
