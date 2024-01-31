package com.goudong.authentication.server.easyexcel.template;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：
 * 用户导出
 * @author chenf
 */
@Data
public class BaseUserExportTemplate {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("序号")
    @ExcelProperty("序号")
    @ColumnWidth(5)
    private Integer sequenceNumber;

    @ApiModelProperty("用户名")
    @ExcelProperty("用户名")
    @ColumnWidth(10)
    private String username;

    @ApiModelProperty("角色列表")
    @ExcelProperty("角色列表")
    @ColumnWidth(20)
    private String roles;

    @ApiModelProperty("用户过期时间")
    @ExcelProperty("过期时间")
    @ColumnWidth(20)
    private Date validTime;

    @ApiModelProperty("激活状态")
    @ExcelProperty("激活状态")
    @ColumnWidth(20)
    private String enabled;

    @ApiModelProperty("锁定状态")
    @ExcelProperty("锁定状态")
    @ColumnWidth(20)
    private String locked;

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
