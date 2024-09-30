package com.goudong.authentication.server.exception;



/**
 * 接口描述：
 * 定义异常枚举的方法
 * @author msi
 */
public interface ExceptionEnumInterface {
    /**
     * 响应码
     */
    int getStatus();
    /**
     * 错误代码
     */
    String getCode();
    /**
     * 客户看见的提示信息
     */
    String getClientMessage();
    /**
     * 服务器日志信息
     */
    String getServerMessage();

    /**
     * 包含状态码
     * @param status http状态码
     * @return true 包含；false 不包含
     */
    default boolean containStatus(int status) {
        return false;
    };
}
