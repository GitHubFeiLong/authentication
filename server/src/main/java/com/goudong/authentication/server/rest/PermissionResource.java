package com.goudong.authentication.server.rest;

import com.goudong.authentication.server.rest.resp.ListAllMenusResp;
import com.goudong.authentication.server.rest.resp.PermissionListPermissionByUsername2SimpleResp;
import com.goudong.authentication.server.rest.req.PermissionListPermissionByUsernameReq;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.authentication.server.service.dto.PermissionDTO;
import com.goudong.authentication.server.service.manager.PermissionManagerService;
import com.goudong.core.lang.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
    @PostMapping("/list-by-username/simple")
    @ApiOperation(value = "获取指定用户权限（角色、角色权限）")
    public Result<PermissionListPermissionByUsername2SimpleResp> listPermissionByUsername2Simple(@RequestBody @Validated PermissionListPermissionByUsernameReq req) {
        return Result.ofSuccess(permissionManagerService.listPermissionByUsername2Simple(req));
    }

    @PostMapping("/list-all-menus")
    @ApiOperation(value = "获取指定用户权限（角色、角色权限）")
    public Result<ListAllMenusResp> listAllMenus() {
        return Result.ofSuccess(permissionManagerService.ListAllMenusResp());
    }


}
