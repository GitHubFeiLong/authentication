package com.goudong.authentication.server.util;// package com.goudong.authentication.common.util;

import com.goudong.authentication.common.constant.CommonConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 类描述：
 *
 * @author chenf
 */
@Slf4j
public class HttpRequestUtil {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 获取请求头中{@code X-App-Id}的值
     * @return 应用Id
     */
    public static Long getXAppId() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String appId = request.getHeader(CommonConst.HTTP_HEADER_X_APP_ID);
        return Long.parseLong(appId);
    }

    /**
     * 获取请求数据
     *
     * @return
     * @paramrequest
     */
    public static String getBody(HttpServletRequest request) {
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8);
            StringBuffer bf = new StringBuffer();
            int len;
            char[] chs = new char[1024];
            while ((len = in.read(chs)) != -1) {
                bf.append(new String(chs, 0, len));
            }
            return bf.toString();
        } catch (Exception e) {
            log.error("请求头部取数据异常：{}", e.getMessage());
            throw new RuntimeException("无效的请求数据");
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (Exception e) {
                    log.error("流关闭异常:{}", e.getMessage());
                }
            }
        }
    }
}
