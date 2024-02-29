package com.goudong.authentication.client.util;


import com.goudong.authentication.client.constant.ApiConst;
import com.goudong.authentication.client.constant.CommonConst;
import okhttp3.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

class OkHttpUtilTest {

    @Test
    void testOkHttp() throws IOException {
        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        String json = "{\"username\": \"zs\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);

        String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCEI0wp13jLL5U3g5Xy1sS7VyowJVCQCAU9cGN42cVulI3XMRw9g+WxV2oOKvzT8M0nbmKnCtQtJj37Nfs7G+I3kKiXAonjmjFL1+o1oC/U6pNvGXCLnsLckxlHgYBXz3pZkhmPsNAP1W7iqb8V6kAp+c3uKFdRI6RYUebrmbS1wrV2vseQAoVjdaOwqmClEtL0WpkofOFUToT/39zwGjnz35JpyPkvVANa84jALEiD//qQ9vrZsrZCTVe+5mKv9dDf+gYzEViHsG6x7TsO29YDpubwNAWQntrjvQ968VeVzeFiNa3G95eT306XWpO2GgrObzEhNpNT6hSsUFYs2jjBAgMBAAECggEAREq1314kaObTuNhCFDqZfLVE8ZMnevkaNDG0x03NsyQF1rTkAor7mFc0UdQ3zRXEAeAUDn8TP65nFEFG8bqshMk3yKwgNciIN8mEm2iJsAH4ZtAAEbjgvv0rcIBmo3twoYBXw6K/NZ+6al0OjDYAKXGY08B3uFCybI8PyK4s7k7Po+gWh8E9Pp2OPWu2NQRF/l4l0b89s4B9rVjSS4+3qh4GBOPMhL/qTia8ENsGD7IUfCmfPW/KF1rH1J+o3bjJqGZsL7UhmSU4jhl9IC5iwW0l7alggmjow+UGrnyLYrCGI884Q3y7X6lfH58xxMMzXMPIOCWeydJoz297+Ce+QQKBgQDHBcOh+DnZLElza0xhq/PWNvbFMzGF/Dh5C5GWQAl0Gxt08SkPIJGR8/Cxumi39IxZGI9gUdZVS4YYWOsJ4pQYBvE8QC5YrWCfpj1PzO9fqtSkZXdpaoVKI0a5mN13vQgQchdTT0ZixlNFBSdAjjTTqBY5jdTO7l5Uz3Lk8kMcaQKBgQCp95mm2j5vbzmTNN5EYZAIlBdr/Ws5027DHkWlwg+s2ngQijqUdUoOrZp0GvijYdKLOZx9+c5uGz1Ppxsth4KDhDlzcgqll41yfpPNGm3GK1pD2dZfyqLjE3OeJlFiEiOont+77HrVnQapU5JfVQ4gXMegC8iQsi0E6DUc8ZyOmQKBgQDBS4zk67QrhALWkZi+qAYLwJhNkNoBeqTSk3TTy+Ainw08t9ib85LOgXD8/MImH+KLyfm+n90ePFrBnOKyWBhjeaKkVOVuhP16MTgNIEpAzSb3gADJJobMKZTQP3Khj7ucVWLoK+99kYdgWo/z9+nAw9jKD0FntUVy84RwbgbWOQKBgBe/jct/cPvW+Z904p2BWEOqlX80QHgq7635m9emPtRzDhWZisfVf4x/ezY/0n/ZmjZzzUbY0ZwW2EE54xVAet7vaauUwqISg+P8haBB0ha/VfRnCooF4S6DOENfrT1btXnJAQ/b4enY7+ZW0/vYAYpKDGZzXwnAq4OlAk7R/4L5AoGBAIlusnsDCnEbpU1LrcrMHUTIqFC4DqDluMmfbCt092VQNDNXSDKIM9DgkPlQ36wwcpKgWtMhM4FEp13JqT4FgULOHWDD7ZUvOI2FceZo92PArMpEnzQKELq6qe64xPDZdCkVuyQw4pjzw4zpOFH6ggBhVZUI2XZ5tsG6nzF3GQwD";

        GoudongAuthenticationClient.init("http://127.0.0.1:8080/api/authentication-server", 1759455546115076096L, "f1d606837922c634", privateKey);

        // 生成令牌
        String authentication = GouDongUtil.generateToken(json);

        Request request = new Request.Builder().url("http://127.0.0.1:8080/api/authentication-server/permission/list-by-username/simple")
                .header("Authorization", authentication)
                .post(body)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String string = response.body().string();
            System.out.println("string = " + string);
        } else {
            String string = response.body().string();
            System.out.println("string = " + string);
        }

    }

    @Test
    void testDeleteUsers() throws IOException {
        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        String json = "[\"1759843949260296192\"]";
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);

        String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCEI0wp13jLL5U3g5Xy1sS7VyowJVCQCAU9cGN42cVulI3XMRw9g+WxV2oOKvzT8M0nbmKnCtQtJj37Nfs7G+I3kKiXAonjmjFL1+o1oC/U6pNvGXCLnsLckxlHgYBXz3pZkhmPsNAP1W7iqb8V6kAp+c3uKFdRI6RYUebrmbS1wrV2vseQAoVjdaOwqmClEtL0WpkofOFUToT/39zwGjnz35JpyPkvVANa84jALEiD//qQ9vrZsrZCTVe+5mKv9dDf+gYzEViHsG6x7TsO29YDpubwNAWQntrjvQ968VeVzeFiNa3G95eT306XWpO2GgrObzEhNpNT6hSsUFYs2jjBAgMBAAECggEAREq1314kaObTuNhCFDqZfLVE8ZMnevkaNDG0x03NsyQF1rTkAor7mFc0UdQ3zRXEAeAUDn8TP65nFEFG8bqshMk3yKwgNciIN8mEm2iJsAH4ZtAAEbjgvv0rcIBmo3twoYBXw6K/NZ+6al0OjDYAKXGY08B3uFCybI8PyK4s7k7Po+gWh8E9Pp2OPWu2NQRF/l4l0b89s4B9rVjSS4+3qh4GBOPMhL/qTia8ENsGD7IUfCmfPW/KF1rH1J+o3bjJqGZsL7UhmSU4jhl9IC5iwW0l7alggmjow+UGrnyLYrCGI884Q3y7X6lfH58xxMMzXMPIOCWeydJoz297+Ce+QQKBgQDHBcOh+DnZLElza0xhq/PWNvbFMzGF/Dh5C5GWQAl0Gxt08SkPIJGR8/Cxumi39IxZGI9gUdZVS4YYWOsJ4pQYBvE8QC5YrWCfpj1PzO9fqtSkZXdpaoVKI0a5mN13vQgQchdTT0ZixlNFBSdAjjTTqBY5jdTO7l5Uz3Lk8kMcaQKBgQCp95mm2j5vbzmTNN5EYZAIlBdr/Ws5027DHkWlwg+s2ngQijqUdUoOrZp0GvijYdKLOZx9+c5uGz1Ppxsth4KDhDlzcgqll41yfpPNGm3GK1pD2dZfyqLjE3OeJlFiEiOont+77HrVnQapU5JfVQ4gXMegC8iQsi0E6DUc8ZyOmQKBgQDBS4zk67QrhALWkZi+qAYLwJhNkNoBeqTSk3TTy+Ainw08t9ib85LOgXD8/MImH+KLyfm+n90ePFrBnOKyWBhjeaKkVOVuhP16MTgNIEpAzSb3gADJJobMKZTQP3Khj7ucVWLoK+99kYdgWo/z9+nAw9jKD0FntUVy84RwbgbWOQKBgBe/jct/cPvW+Z904p2BWEOqlX80QHgq7635m9emPtRzDhWZisfVf4x/ezY/0n/ZmjZzzUbY0ZwW2EE54xVAet7vaauUwqISg+P8haBB0ha/VfRnCooF4S6DOENfrT1btXnJAQ/b4enY7+ZW0/vYAYpKDGZzXwnAq4OlAk7R/4L5AoGBAIlusnsDCnEbpU1LrcrMHUTIqFC4DqDluMmfbCt092VQNDNXSDKIM9DgkPlQ36wwcpKgWtMhM4FEp13JqT4FgULOHWDD7ZUvOI2FceZo92PArMpEnzQKELq6qe64xPDZdCkVuyQw4pjzw4zpOFH6ggBhVZUI2XZ5tsG6nzF3GQwD";

        GoudongAuthenticationClient client = GoudongAuthenticationClient.init("http://127.0.0.1:8080/api/authentication-server", 1759455546115076096L, "f1d606837922c634", privateKey);

        // 生成令牌
        String authentication = GouDongUtil.generateToken(json);

        Request request = new Request.Builder().url(client.getServerUrl() + ApiConst.USER_DELETE_USERS)
                .header(CommonConst.HTTP_HEADER_AUTHORIZATION, authentication)
                .header(CommonConst.HTTP_HEADER_X_APP_ID, String.valueOf(client.getAppId()))
                .delete(body)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String string = response.body().string();
            System.out.println("string = " + string);
        } else {
            String string = response.body().string();
            System.out.println("string = " + string);
        }
    }

}