package com.goudong.authentication.server.exception;

/**
 * 类描述：
 * 字典未找到异常
 * @author chenf
 */
public class DictNotFoundException extends RuntimeException{
    //~fields
    //==================================================================================================================
    private static final long serialVersionUID = -1628637941430042696L;

    //~methods
    //==================================================================================================================
    public DictNotFoundException(String message) {
        super(message);
    }
}
