package com.goudong.authentication.client.api.user.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.goudong.authentication.client.api.user.v1.req.*;
import com.goudong.authentication.client.api.user.v1.resp.BaseUserCreateTokenResp;
import com.goudong.authentication.client.api.user.v1.resp.BaseUserRefreshTokenResp;
import com.goudong.authentication.client.api.user.v1.resp.BaseUserSupplementTokenResp;
import com.goudong.authentication.client.constant.ApiConst;
import com.goudong.authentication.client.constant.CommonConst;
import com.goudong.authentication.client.core.Result;
import com.goudong.authentication.client.dto.BaseUserDTO;
import com.goudong.authentication.client.util.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Collection;

/**
 * 类描述：
 * 用户相关API
 * @author chenf
 */
@Slf4j
public class UserV1Api {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    /**
     * 给指定用户创建令牌
     * @param req   用户信息
     * @return      创建成功的token信息
     */
    public static Result<BaseUserCreateTokenResp> createToken(BaseUserSimpleCreateReq req) {
        // 获取客户端信息
        GoudongAuthenticationClient client = GoudongAuthenticationClient.getClient(req.getAppId());
        final String api = client.getServerUrl() + ApiConst.USER_CREATE_TOKEN;

        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        String json = JsonUtil.toJsonString(req);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);

        // 生成令牌
        String authentication = client.generateToken(json);

        Request request = new Request.Builder().url(api)
                .header(CommonConst.HTTP_HEADER_AUTHORIZATION, authentication)
                .header(CommonConst.HTTP_HEADER_X_APP_ID, String.valueOf(client.getAppId()))
                .post(requestBody)
                .build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            return JsonUtil.toObject(response.body().string(), new TypeReference<Result<BaseUserCreateTokenResp>>() {});
        } catch (IOException e) {
            LogUtil.error(log, () -> "请求接口失败，接口地址：{}, 失败因素：{}", () -> ArrayUtil.create(api, e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    /**
     * 给指定用户创建令牌
     * @param req   用户信息
     * @return      创建成功的token信息
     */
    public static Result<BaseUserRefreshTokenResp> refreshToken(BaseUserRefreshTokenReq req) {
        // 获取客户端信息
        GoudongAuthenticationClient client = GoudongAuthenticationClient.getClient(req.getAppId());
        final String api = client.getServerUrl() + ApiConst.USER_REFRESH_TOKEN;

        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        String json = JsonUtil.toJsonString(req);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);

        // 生成令牌
        String authentication = client.generateToken(json);

        Request request = new Request.Builder().url(api)
                .header(CommonConst.HTTP_HEADER_AUTHORIZATION, authentication)
                .header(CommonConst.HTTP_HEADER_X_APP_ID, String.valueOf(client.getAppId()))
                .post(requestBody)
                .build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            return JsonUtil.toObject(response.body().string(), new TypeReference<Result<BaseUserRefreshTokenResp>>() {});
        } catch (IOException e) {
            LogUtil.error(log, () -> "请求接口失败，接口地址：{}, 失败因素：{}", () -> ArrayUtil.create(api, e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加用户
     * @param req   添加用户参数
     * @return      新建用户信息
     */
    public static Result<BaseUserDTO> simpleCreateUser(BaseUserSimpleCreateReq req) {
        // 获取客户端信息
        GoudongAuthenticationClient client = GoudongAuthenticationClient.getClient(req.getAppId());
        final String api = client.getServerUrl() + ApiConst.USER_SIMPLE_CREATE_USER;

        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        String json = JsonUtil.toJsonString(req);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);

        // 生成令牌
        String authentication = client.generateToken(json);

        Request request = new Request.Builder().url(api)
                .header(CommonConst.HTTP_HEADER_AUTHORIZATION, authentication)
                .header(CommonConst.HTTP_HEADER_X_APP_ID, String.valueOf(client.getAppId()))
                .post(requestBody)
                .build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            return JsonUtil.toObject(response.body().string(), new TypeReference<Result<BaseUserDTO>>() {});
        } catch (IOException e) {
            LogUtil.error(log, () -> "请求接口失败，接口地址：{}, 失败因素：{}", () -> ArrayUtil.create(api, e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量删除用户
     * @param ids       用户id数组
     * @return          true：删除成功；false：删除失败
     */
    public static Result<Boolean> deleteByIds(BaseUserDeleteByIdsReq req) {
        // 获取客户端信息
        GoudongAuthenticationClient client = GoudongAuthenticationClient.getClient(req.getAppId());
        final String api = client.getServerUrl() + ApiConst.USER_DELETE_USERS;

        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        String json = JsonUtil.toJsonString(req.getIds());
        // 生成令牌
        String authentication = GouDongUtil.generateToken(client.getAppId(), client.getSerialNumber(), json, client.getPrivateKey());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);
        Request request = new Request.Builder().url(api)
                .header(CommonConst.HTTP_HEADER_AUTHORIZATION, authentication)
                .header(CommonConst.HTTP_HEADER_X_APP_ID, String.valueOf(client.getAppId()))
                .delete(requestBody)
                .build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            return JsonUtil.toObject(response.body().string(), new TypeReference<Result<Boolean>>() {});
        } catch (IOException e) {
            LogUtil.error(log, () -> "请求接口失败，接口地址：{}, 失败因素：{}", () -> ArrayUtil.create(api, e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    /**
     * 补充token，返回新的令牌
     * @param req   用户id数组
     * @return      新的令牌信息
     */
    public static Result<BaseUserSupplementTokenResp> supplementToken(BaseUserSupplementTokenReq req) {
        // 获取客户端信息
        GoudongAuthenticationClient client = GoudongAuthenticationClient.getClient(req.getAppId());
        final String api = client.getServerUrl() + ApiConst.USER_SUPPLEMENT_TOKEN;

        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        String json = JsonUtil.toJsonString(req);
        // 生成令牌
        String authentication = GouDongUtil.generateToken(client.getAppId(), client.getSerialNumber(), json, client.getPrivateKey());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);
        Request request = new Request.Builder().url(api)
                .header(CommonConst.HTTP_HEADER_AUTHORIZATION, authentication)
                .header(CommonConst.HTTP_HEADER_X_APP_ID, String.valueOf(client.getAppId()))
                .post(requestBody)
                .build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            return JsonUtil.toObject(response.body().string(), new TypeReference<Result<BaseUserSupplementTokenResp>>() {});
        } catch (IOException e) {
            LogUtil.error(log, () -> "请求接口失败，接口地址：{}, 失败因素：{}", () -> ArrayUtil.create(api, e.getMessage()));
            throw new RuntimeException(e);
        }
    }

}
