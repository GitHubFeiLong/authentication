package com.goudong.authentication.server.exception;

/**
 * 接口描述：
 * 定义异常枚举的方法
 * @author msi
 */
public interface ExceptionEnumInterfaceExt extends ExceptionEnumInterface {

    // ~ 定义快捷异常方法
    // =================================================================================================================
    default BasicException client() {
        throw new NoSuchMethodError();
    }

    default BasicException client(String clientMessage){
        throw new NoSuchMethodError();
    }
    default BasicException client(String clientMessage, String serverMessage){
        throw new NoSuchMethodError();
    }
    default BasicException client(String clientMessageTemplate, Object[] clientMessageParams){
        throw new NoSuchMethodError();
    }
    default BasicException client(String clientMessageTemplate, Object[] clientMessageParams, String serverMessage){
        throw new NoSuchMethodError();
    }
    default BasicException client(String clientMessage, String serverMessageTemplate, Object[] serverMessageParams){
        throw new NoSuchMethodError();
    }
    default BasicException client(String clientMessageTemplate, Object[] clientMessageParams, String serverMessageTemplate, Object[] serverMessageParams){
        throw new NoSuchMethodError();
    }

    default BasicException server() {
        throw new NoSuchMethodError();
    }

    default BasicException server(String serverMessage){
        throw new NoSuchMethodError();
    }
    default BasicException server(String clientMessage, String serverMessage){
        throw new NoSuchMethodError();
    }
    default BasicException server(String serverMessageTemplate, Object[] serverMessageParams){
        throw new NoSuchMethodError();
    }
    default BasicException server(String clientMessage, String serverMessageTemplate, Object[] serverMessageParams){
        throw new NoSuchMethodError();
    }

    default BasicException server(String clientMessageTemplate, Object[] clientMessageParams, String serverMessageTemplate, Object[] serverMessageParams){
        throw new NoSuchMethodError();
    }
}
