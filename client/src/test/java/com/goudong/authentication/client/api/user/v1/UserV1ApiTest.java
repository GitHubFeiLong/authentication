package com.goudong.authentication.client.api.user.v1;

import com.goudong.authentication.client.api.user.v1.req.*;
import com.goudong.authentication.client.api.user.v1.resp.BaseUserCreateTokenResp;
import com.goudong.authentication.client.api.user.v1.resp.BaseUserRefreshTokenResp;
import com.goudong.authentication.client.api.user.v1.resp.BaseUserSupplementTokenResp;
import com.goudong.authentication.client.core.Result;
import com.goudong.authentication.client.dto.BaseUserDTO;
import com.goudong.authentication.client.util.CollectionUtil;
import com.goudong.authentication.client.util.GoudongAuthenticationClient;
import com.goudong.authentication.client.util.JsonUtil;
import com.goudong.authentication.client.util.ListUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({})
class UserV1ApiTest {

    @BeforeEach
    void setUp() {
        String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCFEnACjoSveNwEH/hA00+MzQXHW1yLjTaMXHwRYzu1Gb4hXJ6hTWhwOIJzFGL+akAbqswOul8mlwi1/JT4vACM6tt3BY0nX+rXDzywu8GvM2d/mD2v6CGa66XuauQu0RlsKFFmj+YlS7xyWeBeSat+AdZiFkVGECkkJAv151lqpwTr9aV1ejIZxjqRYJLUx452rUT5BL46leavnxl4hYy+p1LvJuu3afoM82adVARIfyiXc3gRFg/WU6l2anczEd5kDKMWLjA8oXn/wARad6NjlVgRVIfWJ9cRN0jPU9ipy4GZDqqLQHluxN7tfJB7yYF9LaNlQtNFdtZzyPFvCEP7AgMBAAECggEALcNbdeWhR98eeuSrsU96Sgev9xE6UFut6LNdj3U68Vr7AeoiO4KtafplEkJukOPlTB5+sonxcwn52uwUJz4wSkXyV4o3MtVPo8n/qx/GygO5ki2++SGxfN+RXt6vvlH2ljV/WPAfy6+yDPlPqd1X9quXGIBkLmYcX+ttmZGINJ7gYmdneMHsPRxKs+dYWlZlTAtK4b+bwmJRHrr1FebtWCssNAmV/ZUUKi7qJgxgd6hgLbvEiuD2jRBSgs3kGrwJ167xFo0AOubrSqM2jk2jCUpJuFH+ZD3aZJNhHfOpxc7qX8tE6xCb+3/XFHRxXASijt46rqXJguvHaMuJzopiEQKBgQDE0fVARFNlXqP2EI5L+cd2/0VRM2iI0mKRYlAHQtlZJA93Bfklh+ydLohj7D+bfUe/iNIe9WM9ZxxgHFtIJ3ONRuZ92UzejC9HGSOt3EZsnQuEvGmP50II/LewotxHrhB62q3ezkTwV9vyIwcCeVX8elD4viXP9DOWNj5WAufINwKBgQCtFYdWODNlfgqTYARpI6oFODM84wAres4WnQXJnlSip+B7Tbo79fr4Du3+emS7Qb7C4TJNpsGMmfuFvnb34YWYEBPlEUjsXyajprlm/blvCRWvC6Ksn3+SCJ6yn8VI8/+4lqZZCQuKCtlba5MuF6V9qA1sPRwOmY6N/qwV3fu4XQKBgQCPBV7pB31ucRpGKXutm+ElCJRPUEMLAY5cxQsQa7RTAVA3lmCYtC/tfC3iJn+GNsBXX04I7xnhObc15AU05m4iYD4Hf0tLgKSuTodNxH5sWKZRGzS+PDcRHstAkOfvMn6JN7UW7d7Mo5uRVYC3fIJOSHfkeRAzD+oYomwjxlK1ewKBgQCsN8SelHMHZF8vfpVE7BiK8v2AcrnDiYCv3XNZxnGeGn3xEkjgyoL5+RjzYWMDM4qUq6/6tZbVyI0M6ZPEVyAvSqgQ/57YmmB+6zCE+qZcheSOdHxbWKR9cJg/UVvil+11BGjmJGrX4q2dGi+q6BphKiqY7UESgdXnEWS2qVaCfQKBgCBM+skFAZiVsaHh9mevceD5dR7bbLkSDmL//oXWwpb2bNKQHHGTwk+iUZ2/tWLW62IOrqiCk5RZJzK6uuVJs2oCAcpBGmwg2TB4OAnHHq5CLrMqAfing3ZKI2aJAT4xX9kZXDs8gNmA69+tomyLygNvYvAIFgC/3MqVoJ5tJZVM";
        GoudongAuthenticationClient.init("http://127.0.0.1:8080/api/authentication-server", 1792440063700226048L,"cb42c8b344364ef3adb848d75eb3842a","5431058dfcdc217c", privateKey);
    }

    @Test
    void testCreateToken() {
        Result<BaseUserCreateTokenResp> app3 = UserV1Api.createToken(BaseUserCreateTokenReq.builder().username("zs").build());
        System.out.println(JsonUtil.toJsonString(app3));
    }

    @Test
    void testRefreshToken() {
        Result<BaseUserRefreshTokenResp> result = UserV1Api.refreshToken(BaseUserRefreshTokenReq.builder().refreshToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOlwiMTc1OTQ1NTU1MzYwNjEwMzA0MFwiLFwiYXBwSWRcIjpcIjFcIixcInJlYWxBcHBJZFwiOlwiMTc1OTQ1NTU0NjExNTA3NjA5NlwiLFwidXNlcm5hbWVcIjpcImFwcDNcIixcInJvbGVzXCI6W1wiUk9MRV9BUFBfQURNSU5cIl0sXCJkZXRhaWxcIjp7fX0iLCJpYXQiOjE3MTA4MzcyMjksImV4cCI6MTcxMDg0NDQyOX0.TOEn2oQO7527ta98ttaXu4JpBBF3c8VqdWZEx89OAKQ").build());
        System.out.println(JsonUtil.toJsonString(result));
    }

    @Test
    void simpleCreateUser() {
        BaseUserSimpleCreateReq req = new BaseUserSimpleCreateReq();
        req.setUsername("小米mix5");
        req.setRoleIds(ListUtil.newArrayList(1762717389965221888L));
        req.setRemark("测试");

        Result<BaseUserDTO> baseUserDTOResult = UserV1Api.simpleCreateUser(req);
        System.out.println("baseUserDTOResult = " + baseUserDTOResult);
    }

    @Test
    void testDeleteByIds() {
        Result<Boolean> booleanResult = UserV1Api.deleteByIds(BaseUserDeleteByIdsReq.builder().ids(ListUtil.newArrayList(1770006966048362496L)).build());
        System.out.println(JsonUtil.toJsonString(booleanResult));
    }

    @Test
    void testSupplementToken() {
        Map<String, Object> detail = new HashMap<>();
        detail.put("key1", "value1");
        detail.put("key2", 2);
        detail.put("key3", ListUtil.newArrayList(1, 2, 3));
        detail.put("key4", "value4");
        Result<BaseUserSupplementTokenResp> result = UserV1Api.supplementToken(BaseUserSupplementTokenReq.builder()
                .token("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOlwiMTc2NTI4OTc2MTYxNjQyMDg2NFwiLFwiYXBwSWRcIjpcIjE3NTk0NTU1NDYxMTUwNzYwOTZcIixcInJlYWxBcHBJZFwiOlwiMTc1OTQ1NTU0NjExNTA3NjA5NlwiLFwidXNlcm5hbWVcIjpcIuWwj-exs21peDRcIixcInJvbGVzXCI6W1wiUk9MRV9ZRVdVWVVBTlwiXSxcImRldGFpbFwiOnt9fSIsImlhdCI6MTcxMDgzOTA1OCwiZXhwIjoxNzEwODQyNjU4fQ.aXe_Oxf6WBtJO1WKzRDvnYWq29-0TCpBhyXDVL0d2w8")
                .detail(detail)
                .build());

        System.out.println(JsonUtil.toJsonString(result));
    }
}