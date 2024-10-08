package com.goudong.authentication.server.lang;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.authentication.common.util.AssertUtil;
import com.goudong.authentication.common.util.StringUtil;
import com.goudong.authentication.server.properties.ApiLogProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 类描述：
 * api接口日志对象
 * @author cfl
 * @version 1.0
 */
@Slf4j
@Data
public class ApiLog {
    //~fields
    //==================================================================================================================
    /**
     * ip地址
     */
    private String ip;
    /**
     * uri
     */
    private String uri;
    /**
     * uri 对应的方法
     */
    private String method;
    /**
     * 自定义的请求头
     */
    private Map<String, String> headParams;
    /**
     * 请求参数对象
     */
    private Object params;
    /**
     * 响应结果对象
     */
    private Object results;

    /**
     * 全局唯一id
     */
    private String tranceId;
    /**
     * 接口成功或失败
     */
    private Boolean successful;
    /**
     * 耗时，单位毫秒
     */
    private Long time;
    //~methods
    //==================================================================================================================

    public ApiLog() {
    }

    /**
     * 转成日志字符串
     * @param apiLogProperties  apiLong配置对象
     * @param objectMapper      对象映射器
     * @return  string 日志字符串
     */
    public String toLogString(ApiLogProperties apiLogProperties, ObjectMapper objectMapper) {
        AssertUtil.isTrue(apiLogProperties.getEnabled(), () -> new RuntimeException("未开启接口日志打印"));

        // 类型开关
        ApiLogProperties.TypeEnabled typeEnabled = apiLogProperties.getTypeEnabled();
        // 限制
        ApiLogProperties.PrintLogLimit printLogLimit = apiLogProperties.getPrintLogLimit();

        Supplier<String> s = () -> "";
        StringBuilder sb = new StringBuilder();
        sb.append("\n----------------------------------------------------------------------------------------------------\n");


        if (typeEnabled.getIp() && ip != null) {
            sb.append("IP        : ").append(ip).append("\n");
        }

        if (typeEnabled.getUri() && uri != null) {
            sb.append("URI       : ").append(uri).append("\n");
        }

        if (typeEnabled.getMethod() && method != null) {
            sb.append("Method    : ").append(method).append("\n");
        }

        if (typeEnabled.getHeadParams() && headParams != null) {
            sb.append("HeadParams: ").append(headParams).append("\n");
        }

        if (typeEnabled.getParams() && params != null) {
            String paramsStr;
            try {
                paramsStr = objectMapper.writeValueAsString(params);
            } catch (Exception e) {
                log.warn("接口参数序列化json失败：{}", e.getMessage());
                paramsStr = params.toString();
            }
            sb.append("Params    : ").append(paramsStr).append("\n");
        }

        if (typeEnabled.getResults() && results != null) {
            // 获取需要打印得长度限制
            int maxResultStrLength = printLogLimit.getResultsLength();
            if (headParams != null) {
                try {
                    maxResultStrLength = StringUtil.isNotBlank(headParams.get("x-api-result-length")) ?
                            Integer.parseInt(headParams.get("x-api-result-length")) : maxResultStrLength;
                } catch (NumberFormatException e) {
                    log.warn("请求头 x-api-result-length 的内容只能是整数类型");
                }
            }

            String resultStr;           // 打印接口返回值
            try {
                if (results instanceof String || results instanceof Exception) {
                    resultStr = results.toString();
                } else {
                    resultStr = objectMapper.writeValueAsString(Optional.ofNullable(results).orElseGet(s));
                }
            } catch (Exception e) {
                log.warn("序列化results json失败：{}", e.getMessage());
                resultStr = results.toString();
            }

            // 获取最终需要打印得返回值
            if (maxResultStrLength >= 0) {
                resultStr = resultStr.length() > maxResultStrLength ? resultStr.substring(0, maxResultStrLength) : resultStr;
            }
            sb.append("Results   : ").append(resultStr).append("\n");
        }

        if (typeEnabled.getTraceId() && tranceId != null) {
            sb.append("traceId   : ").append(Optional.of(tranceId).orElseGet(() -> "null")).append("\n");
        }

        // 接口响应
        if (typeEnabled.getSuccessful() && successful != null) {
            sb.append("successful: ").append(successful).append("\n");
        }

        // 接口时间
        if (typeEnabled.getTime() && time != null) {
            if (time < 1000) {
                sb.append("Time      : ").append(time).append("ms").append("\n");
            } else {
                double doubleValue = BigDecimal.valueOf(time).divide(new BigDecimal(1000), 4, RoundingMode.UP).doubleValue();
                sb.append("Time      : ").append(doubleValue).append("s").append("\n");
            }
        }

        sb.append("----------------------------------------------------------------------------------------------------\n");
        return sb.toString();
    }

    /**
     * 打印日志
     * @param apiLogProperties  apiLong配置对象
     * @param objectMapper  对象映射器
     */
    public void printLogString(ApiLogProperties apiLogProperties, ObjectMapper objectMapper) {
        String logStr = toLogString(apiLogProperties, objectMapper);
        Level level = apiLogProperties.getLevel();
        switch (level) {
            case TRACE:
                log.trace(logStr);
                break;
            case INFO:
                log.info(logStr);
                break;
            case WARN:
                log.warn(logStr);
                break;
            case ERROR:
                log.error(logStr);
                break;
            default:
                log.debug(logStr);
        }
    }
}
