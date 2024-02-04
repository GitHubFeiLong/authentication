package com.goudong.authentication.server.easyexcel.template;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 类描述：
 * 应用导入excel
 * @author chenf
 * @version 1.0
 */
@Data
public class BaseAppImportExcelTemplate {
    //~fields
    //==================================================================================================================
    @ExcelProperty("* 应用名称")
    private String name;

    @ExcelProperty("应用首页")
    private String homePage;

    @ExcelProperty("* 应用状态")
    private String enabled;

    @ExcelProperty("应用备注")
    private String remark;

    //~methods
    //==================================================================================================================
}
