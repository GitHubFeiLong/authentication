package com.goudong.authentication.client.core;

import com.goudong.authentication.client.enums.HttpMethodEnum;
import lombok.Data;

import java.util.List;

/**
 * 类描述：
 * 资源
 * @author cfl
 * @version 1.0
 */
@Data
public class Resource implements ResourceInterface{
    //~fields
    //==================================================================================================================
    /**
     * 资源访问路径
     */
    private String url;

    /**
     * 资源访问方法
     */
    private HttpMethodEnum method;
    //~methods
    //==================================================================================================================
}
