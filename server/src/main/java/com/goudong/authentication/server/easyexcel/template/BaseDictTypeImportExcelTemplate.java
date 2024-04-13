package com.goudong.authentication.server.easyexcel.template;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：
 * excel导入字典类型
 * @author chenf
 */
@Data
public class BaseDictTypeImportExcelTemplate {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("类型编码")
    @ExcelProperty("* 类型编码")
    @ColumnWidth(20)
    private String code;

    @ApiModelProperty("类型名称")
    @ExcelProperty("* 类型名称")
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

    @ApiModelProperty("类型备注")
    @ExcelProperty("类型备注")
    @ColumnWidth(40)
    private String remark;
    //~methods
    //==================================================================================================================
}