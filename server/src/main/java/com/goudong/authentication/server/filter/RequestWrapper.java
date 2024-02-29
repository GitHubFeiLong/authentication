package com.goudong.authentication.server.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * 类描述：
 * 自定义请求包装器，用于获取请求体参数
 * @author chenf
 */
public class RequestWrapper extends HttpServletRequestWrapper {


    //~fields
    //==================================================================================================================
    /**
     * 请求体参数
     */
    private final String body;


    //~methods
    //==================================================================================================================


    /**
     * 构造请求包装器，并获取请求体参数
     * @param request   请求对象
     */
    public RequestWrapper(HttpServletRequest request) {
        super(request);
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            // 缓冲区大小
            int BUFFER_LEN = 128;
            char[] charBuffer = new char[BUFFER_LEN];
            int bytesRead;
            while((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                sb.append(charBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭流
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            body = sb.toString();
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
        return servletInputStream;
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    /**
     * 获取本次请求体参数
     * @return  请求体参数
     */
    public String getBody() {
        return this.body;
    }
}
