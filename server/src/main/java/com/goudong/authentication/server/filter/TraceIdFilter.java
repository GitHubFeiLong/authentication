package com.goudong.authentication.server.filter;


import com.goudong.authentication.common.util.StringUtil;
import com.goudong.authentication.common.util.TraceIdUtil;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 类描述：
 * 拦截器，给请求日志加上追踪id
 * @author cfl
 */
@Component
public class TraceIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            // 添加请求日志的全局唯一id
            String traceId = ((HttpServletRequest) request).getHeader("X-Trace-Id");
            if (StringUtil.isNotBlank(traceId)) {
                TraceIdUtil.put(traceId);
            } else {
                TraceIdUtil.put();
            }
            chain.doFilter(request, response);
        } finally {
            // 清除请求的全局日志id
            TraceIdUtil.remove();
        }
    }

}
