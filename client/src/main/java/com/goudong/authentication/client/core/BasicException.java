package com.goudong.authentication.client.core;

import com.goudong.authentication.client.util.ArrayUtil;
import com.goudong.authentication.client.util.MessageFormatUtil;
import com.goudong.authentication.client.util.StringUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;



/**
 * 类描述：
 * 自定义异常的基类，其它模块的异常继承进行扩展
 *
 * @see Result
 * @author cfl
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class BasicException extends RuntimeException {

    private static final long serialVersionUID = -3324401375994538105L;

    /**
     * http 响应码
     */
    public int status;

    /**
     * 错误代码
     */
    public String code;

    /**
     * 客户端状态码对应信息
     */
    public String clientMessage;

    /**
     * 服务器状态码对应信息
     */
    public String serverMessage;

    /**
     * 额外信息
     */
    public Map dataMap = new HashMap();

    // ~ 常用静态方法
    // =================================================================================================================
    public static BasicException ofResult(Result result) {
        BasicException basicException = new BasicException(result.getStatus(), result.getCode(), result.getClientMessage(), result.getServerMessage())
                .dataMap(result.getDataMap());
        return basicException;
    }
    // ~ builder创建
    // =================================================================================================================
    /**
     * 创建构建者
     * @return
     */
    public static BasicExceptionBuilder builder() {
        return new BasicExceptionBuilder();
    }

    // ~ 常用构造方法
    // =================================================================================================================
    public BasicException() {
        super();
    }


    public BasicException(Throwable cause) {
        super(cause);
    }

    public BasicException(BasicException be) {
        super(be.getClientMessage()+"\t"+be.getServerMessage());
        this.status = be.getStatus();
        this.code = be.getCode();
        this.clientMessage = be.getClientMessage();
        this.serverMessage = be.getServerMessage();
        this.serverMessage = be.getServerMessage();
    }

    public BasicException(String message, Throwable cause) {
        super(message, cause);
    }



    /**
     * 构造方法
     * @param status http状态码
     * @param code 自定义状态码
     * @param clientMessage 客户端显示信息
     * @param serverMessage 服务端日志显示信息
     */
    public BasicException(int status, String code, String clientMessage, String serverMessage) {
        super(clientMessage+"\t"+serverMessage);
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
    }

    /**
     * 构造方法
     * @param status http状态码
     * @param code 自定义状态码
     * @param clientMessage 客户端显示信息
     * @param serverMessage 服务端日志显示信息
     * @param cause 异常对象
     */
    public BasicException(int status, String code, String clientMessage, String serverMessage, Throwable cause) {
        super(clientMessage+"\t"+serverMessage, cause);
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
    }

    //~ 实例方法
    //==================================================================================================================


    /**
     * 设置错误代码
     * @param code
     * @return
     */
    public BasicException code(String code) {
        this.code = code;
        return this;
    }

    /**
     * 设置clientMessage
     * @param clientMessage
     * @return
     */
    public BasicException clientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
        return this;
    }

    /**
     * 设置serverMessage
     * @param serverMessage
     * @return
     */
    public BasicException serverMessage(String serverMessage) {
        this.serverMessage = serverMessage;
        return this;
    }

    /**
     * 设置dataMap
     * @param dataMap
     * @return
     */
    public BasicException dataMap(Map dataMap) {
        this.dataMap = dataMap;
        return this;
    }


    /**
     * 扩展dataMap
     * @param dataMap
     * @return
     */
    public BasicException dataMapPut(Map dataMap) {
        this.dataMap.putAll(dataMap);
        return this;
    }

    /**
     * 扩展dataMap
     * @param key
     * @param value
     * @return
     */
    public BasicException dataMapPut(String key, Object value) {
        this.dataMap.put(key, value);
        return this;
    }

    /**
     * 扩展dataMap
     * @param kv 字符串数组，奇数位是key，偶数位是value
     * @return
     */
    public BasicException dataMapPut(String... kv) {
        // 不是偶数位
        if (kv.length < 2 && kv.length % 2 != 0) {
            throw new IllegalArgumentException("参数kv数组不正确，要是2的倍数，其中奇数是key偶数是value");
        }

        // 步长为2
        for (int i = 0, length = kv.length; i < length; i+=2) {
            String key = kv[i];
            String value = kv[i+1];
            this.dataMap.put(key, value);
        }

        return this;
    }

    /**
     * 类描述：
     * 创建BasicException的构建对象
     * @author cfl
     * @date 2023/3/17 19:34
     * @version 1.0
     */
    public static class BasicExceptionBuilder {

        /**
         * http 响应码
         */
        protected int status;

        /**
         * 错误代码
         */
        protected String code;

        /**
         * 客户端状态码对应信息
         */
        protected String clientMessage;

        /**
         * 客户端模板
         */
        protected String clientMessageTemplate;

        /**
         * 客户端模板参数
         */
        protected Object[] clientMessageParams;

        /**
         * 服务器状态码对应信息
         */
        protected String serverMessage;

        /**
         * 服务端模板
         */
        protected String serverMessageTemplate;

        /**
         * 服务端模板参数
         */
        protected Object[] serverMessageParams;

        /**
         * 额外信息
         */
        protected Map dataMap = new HashMap();

        /**
         * 异常信息
         */
        protected Throwable cause;

        /**
         * 无参构造
         */
        public BasicExceptionBuilder() {
            // 默认设置500状态码
            this.status = 500;
            // 默认设置成失败
            this.code = Result.FAIL;
        }


        /**
         * 设置Http状态码，值必须是正确的4xx或5xx状态码
         * @param status
         * @return
         */
        public BasicExceptionBuilder status(int status) {
            this.status = status;
            return this;
        }

        /**
         * 设置code
         * @param code
         * @return
         */
        public BasicExceptionBuilder code(String code) {
            this.code = code;
            return this;
        }

        /**
         * 设置clientMessage</br>
         * <pre>
         * 注意：
         * 1. 该方法应该避免与{@code clientMessageTemplate}、{@code clientMessageParams}方法一起使用
         * 2. 如果一起使用:
         *  2.1 {@code clientMessage} 在前面，那么{@code clientMessageTemplate}、{@code clientMessageParams}会生效
         *  2.2 {@code clientMessage} 在后面，那么{@code clientMessage}会生效
         * </pre>
         * @param clientMessage
         * @return
         */
        public BasicExceptionBuilder clientMessage(String clientMessage) {
            this.clientMessage = clientMessage;
            this.clientMessageTemplate = null;
            this.clientMessageParams = null;
            return this;
        }

        /**
         * 设置clientMessageTemplate</br>
         * 通常该方法和{@code clientMessageParams}一起使用，这样可以有效避免字符串拼接问题
         * @param clientMessageTemplate
         * @return
         */
        public BasicExceptionBuilder clientMessageTemplate(String clientMessageTemplate) {
            this.clientMessageTemplate = clientMessageTemplate;
            return this;
        }

        /**
         * 设置clientMessageParams</br>
         * 通常该方法和{@code clientMessageTemplate}一起使用，这样可以有效避免字符串拼接问题
         * @param clientMessageParams
         * @return
         */
        public BasicExceptionBuilder clientMessageParams(Object... clientMessageParams) {
            this.clientMessageParams = clientMessageParams;
            return this;
        }

        /**
         * 设置serverMessage
         * <pre>
         * 注意：
         * 1. 该方法应该避免与{@code serverMessageTemplate}、{@code serverMessageParams}方法一起使用
         * 2. 如果一起使用:
         *  2.1 {@code serverMessage} 在前面，那么{@code serverMessageTemplate}、{@code serverMessageParams}会生效
         *  2.2 {@code serverMessage} 在后面，那么{@code serverMessage}会生效
         * </pre>
         * @param serverMessage
         * @return
         */
        public BasicExceptionBuilder serverMessage(String serverMessage) {
            this.serverMessage = serverMessage;
            this.serverMessageTemplate = null;
            this.serverMessageParams = null;
            return this;
        }

        /**
         * 设置serverMessageTemplate</br>
         * 通常该方法和{@code serverMessageParams}一起使用，这样可以有效避免字符串拼接问题
         * @param serverMessageTemplate
         * @return
         */
        public BasicExceptionBuilder serverMessageTemplate(String serverMessageTemplate) {
            this.serverMessageTemplate = serverMessageTemplate;
            return this;
        }

        /**
         * 设置serverMessageParams</br>
         * 通常该方法和{@code serverMessageTemplate}一起使用，这样可以有效避免字符串拼接问题
         * @param serverMessageParams
         * @return
         */
        public BasicExceptionBuilder serverMessageParams(Object... serverMessageParams) {
            this.serverMessageParams = serverMessageParams;
            return this;
        }

        /**
         * 设置dataMap
         * @param dataMap
         * @return
         */
        public BasicExceptionBuilder dataMap(Map dataMap) {
            this.dataMap = dataMap;
            return this;
        }

        /**
         * 设置异常对象
         * @param cause
         * @return
         */
        public BasicExceptionBuilder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        /**
         * 根据成员变量，创建BasicException对象
         * @return BasicException
         */
        public BasicException build() {
            // 优先使用 clientMessageTemplate + clientMessageParams 的组合方式设置 clientMessage
            if (StringUtil.isNotBlank(clientMessageTemplate)) {
                clientMessage = clientMessageTemplate;
                if (ArrayUtil.isNotEmpty(clientMessageParams)) {
                    clientMessage = MessageFormatUtil.format(clientMessageTemplate, clientMessageParams);
                }
            }

            // 优先使用 serverMessageTemplate + serverMessageParams 的组合方式设置 serverMessage
            if (StringUtil.isNotBlank(serverMessageTemplate)) {
                serverMessage = serverMessageTemplate;
                if (ArrayUtil.isNotEmpty(serverMessageParams)) {
                    serverMessage = MessageFormatUtil.format(serverMessageTemplate, serverMessageParams);
                }
            }

            // 创建 BasicException 对象
            return new BasicException(status, code, clientMessage, serverMessage, cause);
        }
    }
}
