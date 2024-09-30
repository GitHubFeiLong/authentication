package com.goudong.authentication.server.rest;

import com.goudong.authentication.server.lang.Result;
import com.goudong.authentication.server.rest.req.PermissionGetUserReq;
import com.goudong.authentication.server.rest.resp.PermissionGetMenusResp;
import com.goudong.authentication.server.rest.resp.PermissionGetRolesMenusResp;
import com.goudong.authentication.server.rest.resp.PermissionGetUserResp;
import com.goudong.authentication.server.rest.resp.PermissionListPermissionByUsername2SimpleResp;
import com.goudong.authentication.server.rest.req.PermissionListPermissionByUsernameReq;
import com.goudong.authentication.server.service.manager.PermissionManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 类描述：
 * 权限资源控制器
 * @author chenf
 * @version 1.0
 */
@RestController
@RequestMapping("/permission")
@Api(tags = "权限")
@Slf4j
public class PermissionResource {
    //~fields
    //==================================================================================================================
    @Resource
    private PermissionManagerService permissionManagerService;

    //~methods
    //==================================================================================================================
    @PostMapping("/get-menus")
    @ApiOperation(value = "获取应用配置的所有菜单")
    public Result<PermissionGetMenusResp> getMenus() {
        return Result.ofSuccess(permissionManagerService.getMenus());
    }

    @PostMapping("/get-roles-menus")
    @ApiOperation(value = "获取应用配置的所有角色和角色对应的菜单", notes = "管理员角色返回所有菜单，不让客户端再进行角色判断")
    public Result<PermissionGetRolesMenusResp> getRolesMenus() {
        return Result.ofSuccess(permissionManagerService.getRolesMenus());
    }

    @PostMapping("/get-user")
    @ApiOperation(value = "获取用户和角色信息")
    public Result<PermissionGetUserResp> getUser(@RequestBody @Validated PermissionGetUserReq req) {
        return Result.ofSuccess(permissionManagerService.getUser(req));
    }


    @PostMapping("/list-by-username/simple")
    @ApiOperation(value = "获取指定用户权限（角色、角色权限）")
    @Deprecated
    public Result<PermissionListPermissionByUsername2SimpleResp> listPermissionByUsername2Simple(@RequestBody @Validated PermissionListPermissionByUsernameReq req) {
        return Result.ofSuccess(permissionManagerService.listPermissionByUsername2Simple(req));
    }

}
