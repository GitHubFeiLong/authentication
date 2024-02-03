package com.goudong.authentication.server.easyexcel.template;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * 菜单导出
 * @author chenf
 */
@Data
public class BaseMenuExportTemplate {
    //~fields
    //==================================================================================================================
    @ExcelProperty("序号")
    @ColumnWidth(6)
    private Integer sequenceNumber;

    @ExcelProperty("菜单标识")
    @ColumnWidth(20)
    private String permissionId;

    @ExcelProperty("上级菜单标识")
    @ColumnWidth(20)
    private String parentPermissionId;

    @ExcelProperty("菜单名称")
    @ColumnWidth(20)
    private String name;

    @ExcelProperty("菜单类型")
    @ColumnWidth(20)
    private String type;

    @ExcelProperty("路由或接口地址")
    @ColumnWidth(20)
    private String path;

    @ExcelProperty("请求方式")
    @ColumnWidth(20)
    private String method;

    @ExcelProperty("菜单元数据")
    @ColumnWidth(20)
    private String meta;

    @ExcelProperty("菜单备注")
    @ColumnWidth(20)
    private String remark;

    //~methods
    //==================================================================================================================
}
