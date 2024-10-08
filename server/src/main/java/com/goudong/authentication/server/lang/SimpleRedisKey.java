package com.goudong.authentication.server.lang;

import org.springframework.data.redis.connection.DataType;

import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * 使用类来处理生成key，暂时还不实用，先留着后面优化
 * @Author e-Feilong.Chen
 * @Date 2022/1/11 12:30
 */
public class SimpleRedisKey extends AbstractRedisKey {
    private static final long serialVersionUID = 6929800213090426273L;

    /**
     * 设置Api接口限流
     * @param sessionId 用户的sessionId
     * @param requestData 请求url和请求参数
     */
    public static final SimpleRedisKey API_REPEAT_KEY = new SimpleRedisKey("goudong-commons:api-repeat:${sessionId}:${requestData}", DataType.STRING, Integer.class);

    /**
     * 类似
     */
    public static final SimpleRedisKey KEY1 = new SimpleRedisKey("key1:${key2}", DataType.STRING, Object.class);

    //~fields
    //==================================================================================================================

    //~construct methods
    //==================================================================================================================
    /**
     * 构造函数，设置key不过期
     *
     * @param key       key模板字符串
     * @param redisType redis数据类型
     * @param javaType  java数据类型
     */
    protected SimpleRedisKey(String key, DataType redisType, Class javaType) {
        super(key, redisType, javaType);
    }

    /**
     * 构造函数，设置key指定时间过期
     *
     * @param key       key模板字符串
     * @param redisType redis数据类型
     * @param javaType  java数据类型
     * @param time      过期时长
     * @param timeUnit  过期时长单位，不能为空，因为过期时间设置需要它进行转换时间
     */
    protected SimpleRedisKey(String key, DataType redisType, Class javaType, long time, TimeUnit timeUnit) {
        super(key, redisType, javaType, time, timeUnit);
    }


    //~methods
    //==================================================================================================================
}
