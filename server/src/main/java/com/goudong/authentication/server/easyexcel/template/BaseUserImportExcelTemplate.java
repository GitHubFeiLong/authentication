package com.goudong.authentication.server.easyexcel.template;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.goudong.authentication.server.domain.BaseRole;
import com.goudong.authentication.server.properties.AuthenticationServerProperties;
import lombok.Data;

import java.util.List;

/**
 * 类描述：
 * 用户导入excel
 * @author chenf
 */
@Data
public class BaseUserImportExcelTemplate {
    //~fields
    //==================================================================================================================
    /**
     * 用户名：同一应用下，用户名唯一。
     */
    @ExcelProperty("* 用户名")
    private String username;

    /**
     * 密码：如果填写了密码（至少填写6位），那用户需要使用指定密码进行登录；如果未填写密码，系统会给用户初始一个默认密码。
     * @see AuthenticationServerProperties.AppInner#getUserDefaultPassword()
     */
    @ExcelProperty("密码")
    private String password;

    /**
     * 角色：应该填写本应用下已存在的角色（可以使用英文逗号分割多个角色名）。
     */
    @ExcelProperty("* 角色")
    private String roles;

    /**
     * 有效期：用户有效截止时间，超过有效期的用户不能登录。
     * <ol>
     *     <li>填写具体的日期时间，格式 yyyy-MM-dd HH:mm:ss。</li>
     *     <li>填写具体的日期，格式yyyy-MM-dd（时间是23:59:59）。</li>
     *     <li>填写具体的时间，格式HH:mm:ss（日期是当天）。</li>
     * </ol>
     */
    @ExcelProperty("* 有效期")
    private String validTime;

    /**
     * 激活状态：分为“已激活”和“未激活”，未激活的用户不能登录。
     * <ol>
     *     <li>已激活：true</li>
     *     <li>未激活：false</li>
     * </ol>
     */
    @ExcelProperty("* 激活状态")
    private String enabled;

    /**
     * 锁定状态：分为“已锁定”和“未锁定”，当用户在某些情况下被认为是不安全的（例如，多次输入错误密码或账户被其他用户报告为恶意），他们的账户将被锁定。锁定账户的目的是防止用户在不安全的情况下使用其账户。在账户被锁定期间，用户无法登录，并且必须在特定的时间段内重置密码或联系管理员以解锁账户。
     * <ol>
     *     <li>未锁定：false</li>
     *     <li>已锁定：true</li>
     * </ol>
     */
    @ExcelProperty("* 锁定状态")
    private String locked;

    /**
     * 用户备注
     */
    @ExcelProperty("备注")
    private String remark;

    /**
     * 将解析的角色设置到属性。
     */
    @ExcelIgnore
    private List<BaseRole> baseRoles;
    //~methods
    //==================================================================================================================
}
