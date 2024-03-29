package com.goudong.authentication.server.util;

import cn.zhxu.bs.util.MapBuilder;
import cn.zhxu.bs.util.MapUtils;
import com.goudong.authentication.server.rest.req.search.BasePage;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 类描述：
 *
 * @Author Administrator
 * @Version 1.0
 */
public class BeanSearcherUtil {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * <pre>
     *     获取 search 的map参数
     * </pre>
     * @param req 请求对象
     * @return beanSearch的查询参数，不包含前端的自定义查询条件
     */
    public static Map<String, Object> getParaMap(Object req) {
        return getMapBuilder(req).build();
    }

    /**
     * <pre>
     *     获取 search 的map参数
     * </pre>
     * @param req 请求对象
     * @return beanSearch的查询参数，不包含前端的自定义查询条件
     */
    public static Map<String, Object> getParaMap(BasePage req) {
        return getMapBuilder(req).page(req.getPage(), req.getSize()).build();
    }

    /**
     * 将{@code req}转换成{@code MapBuilder}对象
     * @param req 转换对象
     * @return {@code MapBuilder}对象
     */
    private static MapBuilder getMapBuilder(Object req) {
        MapBuilder builder = MapUtils.builder();
        Class<?> clazz = req.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            // 字段名
            String name = declaredField.getName();
            try {
                Object o = declaredField.get(req);
                builder.field(name, o);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return builder;
    }
}
