package com.goudong.authentication.server.config;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * 接口描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/7/24 10:42
 */
public interface ErrorAttributesService{
    Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options);
}
