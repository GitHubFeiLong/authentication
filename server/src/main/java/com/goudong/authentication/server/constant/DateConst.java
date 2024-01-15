package com.goudong.authentication.server.constant;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 类描述：
 * 日期相关的常量
 * @author msi
 * @version 1.0
 * @date 2022/1/8 17:38
 */
public class DateConst {

    /**
     * 日期时间格式 yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期时间格式 yyyyMMddHHmmss
     */
    public static final String DATE_TIME_FORMATTER_SHORT = "yyyyMMddHHmmss";

    /**
     * 日期格式
     */
    public static final String DATE_FORMATTER = "yyyy-MM-dd";
    /**
     * 时间格式
     */
    public static final String TIME_FORMATTER = "HH:mm:ss";

    /**
     * 日期时间格式Formatter yyyy-MM-dd HH:mm:ss
     */
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_TIME_FORMATTER);
    /**
     * 日期时间格式格式Formatter yyyy-MM-dd HH:mm:ss
     * @see DateConst#DATE_TIME_FORMATTER
     */
    public static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER);

    /**
     * 最小时间 1970-01-01 00:00:00
     */
    public static final Date MIN_DATE_TIME = DateUtil.parse("1970-01-01 00:00:00", DatePattern.NORM_DATETIME_FORMATTER);

    /**
     * 最大时间（系统内使用它作为”永久“） 9999-12-31 12:59:59
     */
    public static final Date MAX_DATE_TIME = DateUtil.parse("9999-12-31 12:59:59", DatePattern.NORM_DATETIME_FORMATTER);
}