package com.goudong.authentication.common.util;

/**
 * 类描述：
 * 数组工具类
 * @author cfl
 * @version 1.0
 */
public class ArrayUtil {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    /**
     * 判断数组是否是空数组
     * @param arr   数组
     * @return  true-是空数组，false-不是空数组
     */
    public static boolean isEmpty(Object[] arr) {
        return (arr == null || arr.length == 0);
    }

    /**
     * 判断数组是否不是空数组
     * @param arr   数组
     * @return   true-不是空数组，false-是空数组
     */
    public static boolean isNotEmpty(Object[] arr) {
        return !ArrayUtil.isEmpty(arr);
    }

    /**
     * 创建一个数组对象
     * @param elements  对象集合
     * @return  对象数组
     */
    public static Object[] create(Object... elements) {
        return elements;
    }
}
