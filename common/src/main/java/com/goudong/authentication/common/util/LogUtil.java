package com.goudong.authentication.common.util;

import com.goudong.authentication.common.function.ObjectArraySupplier;
import com.goudong.authentication.common.function.StringSupplier;
import org.slf4j.Logger;

/**
 * 类描述：
 * 打印日志，根据门面的开关进行判断打印
 * @author chenf
 */
public class LogUtil {

    /**
     * 判断是否打开debug，打印日志
     * @param log 日志对象
     * @param logTemplate 模板
     * @param param 参数
     */
    public static void debug(Logger log, String logTemplate, Object... param){
        if (log.isDebugEnabled()) {
            log.debug(logTemplate, param);
        }
    }

    /**
     * 判断是否打开debug，打印日志
     * @param log 日志对象
     * @param logTemplate 模板
     */
    public static void debug(Logger log, StringSupplier logTemplate){
        if (log.isDebugEnabled()) {
            log.debug(logTemplate.get());
        }
    }

    /**
     * 判断是否打开debug，打印日志
     * @param log 日志对象
     * @param logTemplate 模板
     * @param param 参数
     */
    public static void debug(Logger log, StringSupplier logTemplate, ObjectArraySupplier param){
        if (log.isDebugEnabled()) {
            log.debug(logTemplate.get(), param.get());
        }
    }

    /**
     * 判断是否打开info，打印日志
     * @param log 日志对象
     * @param logTemplate 模板
     * @param param 参数
     */
    public static void info(Logger log, String logTemplate, Object... param){
        if (log.isInfoEnabled()) {
            log.info(logTemplate, param);
        }
    }

    /**
     * 判断是否打开info，打印日志
     * @param log 日志对象
     * @param logTemplate 模板
     */
    public static void info(Logger log, StringSupplier logTemplate){
        if (log.isInfoEnabled()) {
            log.info(logTemplate.get());
        }
    }

    /**
     * 判断是否打开info，打印日志
     * @param log 日志对象
     * @param logTemplate 模板
     * @param param 参数
     */
    public static void info(Logger log, StringSupplier logTemplate, ObjectArraySupplier param){
        if (log.isInfoEnabled()) {
            log.info(logTemplate.get(), param.get());
        }
    }

    /**
     * 判断是否打开warn，打印日志
     * @param log 日志对象
     * @param logTemplate 模板
     * @param param 参数
     */
    public static void warn(Logger log, String logTemplate, Object... param){
        if (log.isWarnEnabled()) {
            log.warn(logTemplate, param);
        }
    }

    /**
     * 判断是否打开warn，打印日志
     * @param log 日志对象
     * @param logTemplate 模板
     */
    public static void warn(Logger log, StringSupplier logTemplate){
        if (log.isWarnEnabled()) {
            log.warn(logTemplate.get());
        }
    }

    /**
     * 判断是否打开warn，打印日志
     * @param log 日志对象
     * @param logTemplate 模板
     * @param param 参数
     */
    public static void warn(Logger log, StringSupplier logTemplate, ObjectArraySupplier param){
        if (log.isWarnEnabled()) {
            log.warn(logTemplate.get(), param.get());
        }
    }

    /**
     * 判断是否打开error，打印日志
     * @param log 日志对象
     * @param logTemplate 模板
     * @param param 参数
     */
    public static void error(Logger log, String logTemplate, Object... param){
        if (log.isErrorEnabled()) {
            log.error(logTemplate, param);
        }
    }

    /**
     * 判断是否打开error，打印日志
     * @param log 日志对象
     * @param logTemplate 模板
     */
    public static void error(Logger log, StringSupplier logTemplate){
        if (log.isErrorEnabled()) {
            log.warn(logTemplate.get());
        }
    }

    /**
     * 判断是否打开error，打印日志
     * @param log 日志对象
     * @param logTemplate 模板
     * @param param 参数
     */
    public static void error(Logger log, StringSupplier logTemplate, ObjectArraySupplier param){
        if (log.isErrorEnabled()) {
            log.warn(logTemplate.get(), param.get());
        }
    }

}
