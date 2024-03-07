package com.goudong.authentication.client.api.user.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.goudong.authentication.client.api.user.v1.req.BaseUserSimpleCreateReq;
import com.goudong.authentication.client.constant.ApiConst;
import com.goudong.authentication.client.constant.CommonConst;
import com.goudong.authentication.client.core.Result;
import com.goudong.authentication.client.dto.BaseUserDTO;
import com.goudong.authentication.client.util.GouDongUtil;
import com.goudong.authentication.client.util.GoudongAuthenticationClient;
import com.goudong.authentication.client.util.JsonUtil;
import com.goudong.authentication.client.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

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
     * 添加用户
     * @param req   添加用户参数
     * @return      新建用户信息
     */
    public static Result<BaseUserDTO> simpleCreateUser(BaseUserSimpleCreateReq req) {
        // 获取客户端信息
        GoudongAuthenticationClient client = GoudongAuthenticationClient.getClient();
        final String api = client.getServerUrl() + ApiConst.USER_SIMPLE_CREATE_USER;

        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        String json = JsonUtil.toJsonString(req);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);

        // 生成令牌
        String authentication = GouDongUtil.generateToken(json);

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
            log.error("请求接口失败，接口地址：{}, 失败因素：{}", api, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量删除用户
     * @param ids       用户id数组
     * @return          true：删除成功；false：删除失败
     */
    public static Result<Boolean> deleteByIds(Collection<Long> ids) {
        // 获取客户端信息
        GoudongAuthenticationClient client = GoudongAuthenticationClient.getClient();
        final String api = client.getServerUrl() + ApiConst.USER_DELETE_USERS;

        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        String json = JsonUtil.toJsonString(ids);
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
            log.error("请求接口失败，接口地址：{}, 失败因素：{}", api, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
