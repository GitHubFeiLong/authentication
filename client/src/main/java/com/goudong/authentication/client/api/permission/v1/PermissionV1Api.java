package com.goudong.authentication.client.api.permission.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.goudong.authentication.client.api.permission.v1.req.PermissionListPermissionByUsernameReq;
import com.goudong.authentication.client.api.permission.v1.resp.PermissionListPermissionByUsername2SimpleResp;
import com.goudong.authentication.client.constant.ApiConst;
import com.goudong.authentication.client.constant.CommonConst;
import com.goudong.authentication.client.core.Result;
import com.goudong.authentication.client.util.GouDongUtil;
import com.goudong.authentication.client.util.GoudongAuthenticationClient;
import com.goudong.authentication.client.util.JsonUtil;
import com.goudong.authentication.client.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

/**
 * 类描述：
 * 权限API
 * @author chenf
 */
@Slf4j
public class PermissionV1Api {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 获取指定用户权限（角色、角色权限）
     * @param req       被查询用户
     * @return          用户权限
     */
    public static Result<PermissionListPermissionByUsername2SimpleResp> listPermissionByUsername2Simple(PermissionListPermissionByUsernameReq req) {
        // 获取客户端信息
        GoudongAuthenticationClient client = GoudongAuthenticationClient.getClient();
        final String api = client.getServerUrl() + ApiConst.PERMISSION_LIST_BY_USERNAME;

        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        String json = JsonUtil.toJsonString(req);
        // 生成令牌
        String authentication = GouDongUtil.generateToken(json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);
        Request request = new Request.Builder().url(api)
                .header(CommonConst.HTTP_HEADER_AUTHORIZATION, authentication)
                .header(CommonConst.HTTP_HEADER_X_APP_ID, String.valueOf(client.getAppId()))
                .post(requestBody)
                .build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            return JsonUtil.toObject(response.body().string(), new TypeReference<Result<PermissionListPermissionByUsername2SimpleResp>>() {});
        } catch (IOException e) {
            log.error("请求接口失败，接口地址：{}, 失败因素：{}", api, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
