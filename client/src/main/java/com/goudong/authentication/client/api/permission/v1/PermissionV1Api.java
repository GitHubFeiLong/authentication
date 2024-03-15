package com.goudong.authentication.client.api.permission.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.goudong.authentication.client.api.permission.v1.req.GetMenusReq;
import com.goudong.authentication.client.api.permission.v1.req.GetRolesMenusReq;
import com.goudong.authentication.client.api.permission.v1.req.GetUserReq;
import com.goudong.authentication.client.api.permission.v1.req.PermissionListPermissionByUsernameReq;
import com.goudong.authentication.client.api.permission.v1.resp.GetMenusResp;
import com.goudong.authentication.client.api.permission.v1.resp.GetRolesMenusResp;
import com.goudong.authentication.client.api.permission.v1.resp.GetUserResp;
import com.goudong.authentication.client.api.permission.v1.resp.PermissionListPermissionByUsername2SimpleResp;
import com.goudong.authentication.client.constant.ApiConst;
import com.goudong.authentication.client.constant.CommonConst;
import com.goudong.authentication.client.core.Result;
import com.goudong.authentication.client.util.*;
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
     * 获取应用下的所有菜单
     * @param req   请求参数
     * @return      应用下的所有菜单
     */
    public static Result<GetMenusResp> getMenus(GetMenusReq req) {
        // 获取客户端信息
        GoudongAuthenticationClient client = GoudongAuthenticationClient.getClient(req.getAppId());
        final String api = client.getServerUrl() + ApiConst.PERMISSION_GET_MENUS;

        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        String json = "";
        // 生成令牌
        String authentication = client.generateToken(json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);
        Request request = new Request.Builder().url(api)
                .header(CommonConst.HTTP_HEADER_AUTHORIZATION, authentication)
                .header(CommonConst.HTTP_HEADER_X_APP_ID, String.valueOf(client.getAppId()))
                .post(requestBody)
                .build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            return JsonUtil.toObject(response.body().string(), new TypeReference<Result<GetMenusResp>>() {});
        } catch (IOException e) {
            LogUtil.error(log, () -> "请求接口失败，接口地址：{}, 失败因素：{}", () -> ArrayUtil.create( api, e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取应用配置的所有角色和菜单
     *
     * @param req 应用ID
     * @return 应用的所有角色信息及角色对应的菜单
     */
    public static Result<GetRolesMenusResp> getRolesMenus(GetRolesMenusReq req) {
        // 获取客户端信息
        GoudongAuthenticationClient client = GoudongAuthenticationClient.getClient(req.getAppId());
        final String api = client.getServerUrl() + ApiConst.PERMISSION_GET_ROLES_MENUS;

        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        String json = "";
        // 生成令牌
        String authentication = client.generateToken(json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);
        Request request = new Request.Builder().url(api)
                .header(CommonConst.HTTP_HEADER_AUTHORIZATION, authentication)
                .header(CommonConst.HTTP_HEADER_X_APP_ID, String.valueOf(client.getAppId()))
                .post(requestBody)
                .build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            return JsonUtil.toObject(response.body().string(), new TypeReference<Result<GetRolesMenusResp>>() {});
        } catch (IOException e) {
            LogUtil.error(log, () -> "请求接口失败，接口地址：{}, 失败因素：{}", () -> ArrayUtil.create( api, e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取用户信息
     * @param req  用户信息
     * @return  用户信息，包含用户基本信息和拥有的角色信息
     */
    public static Result<GetUserResp> getUser(GetUserReq req){
        // 获取客户端信息
        GoudongAuthenticationClient client = GoudongAuthenticationClient.getClient(req.getAppId());
        final String api = client.getServerUrl() + ApiConst.PERMISSION_GET_USER;

        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        String json = JsonUtil.toJsonString(req);
        // 生成令牌
        String authentication = client.generateToken(json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);
        Request request = new Request.Builder().url(api)
                .header(CommonConst.HTTP_HEADER_AUTHORIZATION, authentication)
                .header(CommonConst.HTTP_HEADER_X_APP_ID, String.valueOf(client.getAppId()))
                .post(requestBody)
                .build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            return JsonUtil.toObject(response.body().string(), new TypeReference<Result<GetUserResp>>() {});
        } catch (IOException e) {
            LogUtil.error(log, () -> "请求接口失败，接口地址：{}, 失败因素：{}", () -> ArrayUtil.create( api, e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定用户权限（角色、角色权限）
     * @param req       被查询用户
     * @return          用户权限
     */
    @Deprecated
    public static Result<PermissionListPermissionByUsername2SimpleResp> listPermissionByUsername2Simple(PermissionListPermissionByUsernameReq req) {
        // 获取客户端信息
        GoudongAuthenticationClient client = GoudongAuthenticationClient.getClient(req.getAppId());
        final String api = client.getServerUrl() + ApiConst.PERMISSION_LIST_BY_USERNAME;

        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        String json = JsonUtil.toJsonString(req);
        // 生成令牌
        String authentication = client.generateToken(json);
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
            LogUtil.error(log, () -> "请求接口失败，接口地址：{}, 失败因素：{}", () -> ArrayUtil.create(api, e.getMessage()));
            throw new RuntimeException(e);
        }
    }
}
