package com.goudong.authentication.client.interceptor;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

/**
 * 类描述：
 * OKHttp拦截器，打印请求响应日志
 * @author chenf
 */
public class OKHttpPrintLogInterceptor implements Interceptor {

    //~fields
    //==================================================================================================================
    private static final Logger log = LoggerFactory.getLogger(OKHttpPrintLogInterceptor.class);


    //~methods
    //==================================================================================================================

    /**
     * OKHttp拦截器，打印请求响应日志，加上请求头
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        final String START_END = "\n-------------------------------------------------------------\n";

        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        HttpUrl.Builder urlBuilder = request.url().newBuilder();

        StringBuilder sb = new StringBuilder();
        sb.append(START_END);
        sb.append("请求地址：").append(request.url()).append("\n");
        sb.append("请求方式：").append(request.method()).append("\n");
        sb.append("请求头：\n");
        request.headers().toMultimap().forEach((k, v) -> sb.append("\t").append(k).append(":").append(v).append("\n"));
        sb.append("请求参数：\n");

        StringBuilder paramSb = new StringBuilder();
        if ("GET".equals(request.method())) { // GET方法
            // 这里可以添加一些公共get参数
            HttpUrl httpUrl = urlBuilder.build();
            // 打印所有get参数
            Set<String> paramKeys = httpUrl.queryParameterNames();
            for (String key : paramKeys) {
                String value = httpUrl.queryParameter(key);
                paramSb.append(key).append("=").append(value).append("&");
            }
            if (paramSb.length() > 0) {
                paramSb.delete(paramSb.length() - 1, paramSb.length());
            }

            sb.append(paramSb);
            // 将最终的url填充到request中
            requestBuilder.url(httpUrl);
        } else if ("POST".equals(request.method()) || "PUT".equals(request.method())) { // POST|GET方法
            // 把已有的post参数添加到新的构造器
            if (request.body() instanceof FormBody) {
                FormBody formBody = (FormBody) request.body();
                for (int i = 0; i < formBody.size(); i++) {
                    paramSb.append(formBody.encodedName(i)).append("=").append( formBody.encodedValue(i)).append(",");
                }

                if (paramSb.length() > 0) {
                    paramSb.delete(paramSb.length() - 1, paramSb.length());
                }
            }
        }

        Response response = chain.proceed(request);

        //处理响应报文打印
        MediaType contentType = null;
        String bodyString = null;
        if (response.body() != null) {
            contentType = response.body().contentType();
            bodyString = response.body().string();
        }
        sb.append("请求状态：").append(response.isSuccessful() ? "请求成功" : "请求失败").append(";响应码").append(response.code()).append("\n");
        sb.append("响应参数：").append(bodyString).append("\n");
        sb.append(START_END);
        log.debug(sb.toString());
        if (response.body() != null) {// 深坑！打印body后原ResponseBody会被清空，需要重新设置body
            ResponseBody body = ResponseBody.create(contentType, bodyString);
            return response.newBuilder().body(body).build();
        } else {
            return response;
        }
    }
}
