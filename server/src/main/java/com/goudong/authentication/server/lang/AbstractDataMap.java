package com.goudong.authentication.server.lang;

import lombok.Data;

/**
 * 类描述：
 * 扩展接口的返回数据,可以继承该类进行扩充
 * @author msi
 */
@Data
public class AbstractDataMap {

    //~fields
    //==================================================================================================================
    /**
     * 不处理错误信息
     * 接口返回异常时，前端根据该字段进行判断是否处理错误信息，比如弹窗提示信息
     */
    private Boolean doNotHandleErrorMessage;

    //~methods
    //==================================================================================================================

}
