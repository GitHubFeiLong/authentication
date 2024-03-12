package com.goudong.authentication.client.core;

import com.goudong.authentication.client.enums.HttpMethodEnum;

import java.util.List;

/**
 * 类描述：
 * 资源
 * @author cfl
 * @version 1.0
 */
public interface ResourceInterface {

    /**
     * 获取资源访问路径
     * @return
     */
    String getUrl();

    /**
     * 获取资源访问方法
     * @return
     */
    HttpMethodEnum getMethod();
}
