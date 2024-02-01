package com.goudong.authentication.server.easyexcel.template;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：
 * 角色导出
 * @author chenf
 */
@Data
public class BaseRoleExportTemplate {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("序号")
    @ExcelProperty("序号")
    @ColumnWidth(6)
    private Integer sequenceNumber;

    @ApiModelProperty("角色名称")
    @ExcelProperty("角色名称")
    @ColumnWidth(20)
    private String name;

    @ApiModelProperty("用户数量")
    @ExcelProperty("用户数量")
    @ColumnWidth(12)
    private Integer userNumber;

    @ApiModelProperty("备注")
    @ExcelProperty("备注")
    @ColumnWidth(40)
    private String remark;

    @ApiModelProperty("创建时间")
    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    private Date createdDate;
    //~methods
    //==================================================================================================================
}
