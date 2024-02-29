package com.goudong.authentication.server.filter;


import com.goudong.authentication.server.constant.CommonConst;
import com.goudong.authentication.server.constant.HttpHeaderConst;
import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.pojo.GoudongSHA256withRSARequestHeaderParameter;
import com.goudong.authentication.server.service.dto.BaseAppCertDTO;
import com.goudong.authentication.server.service.manager.BaseAppManagerService;
import com.goudong.boot.web.core.BasicException;
import com.goudong.core.lang.Result;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * 类描述：
 * 过滤器 请求参数验签
 * @author chenf
 */
@Slf4j
@Component
public class HttpRequestParameterVerifyFilter extends OncePerRequestFilter {
    //~fields
    //==================================================================================================================
    @Resource
    private BaseAppManagerService baseAppManagerService;

    //~methods
    //==================================================================================================================

    /**
     * 对请求进行验签
     * @param request           请求对象
     * @param response          响应对象
     * @param filterChain       过滤器链
     * @throws ServletException 异常
     * @throws IOException      IO异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String contentType = request.getContentType();
        // 获取请求头
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtil.isNotBlank(authorization) && authorization.startsWith(CommonConst.AUTHORIZATION_GOUDONG_MODEL)) {
            log.info("当前请求头的认证模式是{}", CommonConst.AUTHORIZATION_GOUDONG_MODEL);
            if (contentType != null && contentType.toLowerCase().startsWith(CommonConst.CONTENT_TYPE_APPLICATION_JSON)) {
                log.info("当前请求参数类型是：{}", CommonConst.CONTENT_TYPE_APPLICATION_JSON);
                GoudongSHA256withRSARequestHeaderParameter parameter = GoudongSHA256withRSARequestHeaderParameter.getInstance(authorization);
                // 查询证书信息
                BaseAppCertDTO baseAppCertDTO = baseAppManagerService.getCertBySerialNumber(parameter.getSerialNumber());
                AssertUtil.isNotNull(baseAppCertDTO, () -> BasicException.client("证书序列号无效"));
                AssertUtil.isTrue(Objects.equals(baseAppCertDTO.getAppId(), parameter.getAppId()), () -> BasicException.client("证书无效").serverMessage("应用和证书不匹配"));
                AssertUtil.isTrue(Objects.equals(baseAppCertDTO.getAppId(), parameter.getAppId()), () -> BasicException.client("证书无效").serverMessage("应用和证书不匹配"));
                AssertUtil.isTrue(baseAppCertDTO.getValidTime().after(new Date()), () -> BasicException.client("证书无效").serverMessage("证书已过期"));

                RequestWrapper requestWrapper = new RequestWrapper(request);
                // 加签时，如果body是null就转换成空字符串拼接。这里也要转换下。
                String body = Optional.ofNullable(requestWrapper.getBody()).orElseGet(() -> "");
                // 验签
                parameter.verify(body, baseAppCertDTO.getCert());

                // 将新包装的请求对象做参数
                filterChain.doFilter(requestWrapper, response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
