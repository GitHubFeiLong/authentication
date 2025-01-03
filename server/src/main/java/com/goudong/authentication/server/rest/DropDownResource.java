package com.goudong.authentication.server.rest;

import com.goudong.authentication.server.constant.SwaggerConst;
import com.goudong.authentication.server.lang.PageResult;
import com.goudong.authentication.server.lang.Result;
import com.goudong.authentication.server.rest.req.BaseDictDropDownReq;
import com.goudong.authentication.server.rest.req.BaseDictDropDownResp;
import com.goudong.authentication.server.rest.req.BaseDictTypeDropDownReq;
import com.goudong.authentication.server.rest.req.BaseDictTypeDropDownResp;
import com.goudong.authentication.server.rest.req.BaseAppDropDownReq;
import com.goudong.authentication.server.rest.req.BaseRoleDropDownReq;
import com.goudong.authentication.server.rest.req.BaseUserDropDownReq;
import com.goudong.authentication.server.rest.resp.BaseRoleDropDownResp;
import com.goudong.authentication.server.rest.resp.BaseUserDropDownResp;
import com.goudong.authentication.server.service.manager.BaseAppManagerService;
import com.goudong.authentication.server.service.manager.BaseDictManagerService;
import com.goudong.authentication.server.service.manager.BaseRoleManagerService;
import com.goudong.authentication.server.service.manager.BaseUserManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 类描述：
 * 下拉菜单
 * @author cfl
 */
@RestController
@RequestMapping("/drop-down")
@Api(tags = "下拉管理")
public class DropDownResource {
    //~fields
    //==================================================================================================================
    @Resource
    private BaseAppManagerService baseAppManagerService;

    @Resource
    private BaseUserManagerService baseUserManagerService;

    @Resource
    private BaseRoleManagerService baseRoleManagerService;

    @Resource
    private BaseDictManagerService baseDictManagerService;

    //~methods
    //==================================================================================================================
    @GetMapping("/base-app")
    @ApiOperation(value = "应用下拉(条件限制)", notes = "需要登录，显示当前用户能查询到的应用，超级管理员查询所有，管理员只能查询本应用", tags = SwaggerConst.DROP_DOWN_GROUP_NAME)
    public Result<List<BaseAppDropDownReq>> appDropDown(@Validated BaseAppDropDownReq req) {
        return Result.ofSuccess(baseAppManagerService.appDropDown(req));
    }

    @GetMapping("/base-app/all-drop-down")
    @ApiOperation(value = "应用下拉(无条件限制)", notes = "不需要登录，显示所有应用，根据创建时间排序", tags = SwaggerConst.DROP_DOWN_GROUP_NAME)
    public Result<List<BaseAppDropDownReq>> allAppDropDown(@Validated BaseAppDropDownReq req) {
        return Result.ofSuccess(baseAppManagerService.allDropDown(req));
    }

    @GetMapping("/base-user/page")
    @ApiOperation(value = "用户下拉(分页+条件限制)", notes = "需要登录，显示用户所在应用下（真实应用）的用户列表(分页查询)", tags = SwaggerConst.DROP_DOWN_GROUP_NAME)
    public Result<PageResult<BaseUserDropDownResp>> userDropDown(@Validated BaseUserDropDownReq req) {
        return Result.ofSuccess(baseUserManagerService.userDropDown(req));
    }

    @GetMapping("/base-role/page")
    @ApiOperation(value = "角色下拉(分页+条件限制)", notes = "需要登录，显示用户所在应用下（真实应用）的角色列表(分页查询)", tags = SwaggerConst.DROP_DOWN_GROUP_NAME)
    public Result<PageResult<BaseRoleDropDownResp>> roleDropDown(@Validated BaseRoleDropDownReq req) {
        return Result.ofSuccess(baseRoleManagerService.roleDropDown(req));
    }

    @GetMapping("/base-dict-type/page")
    @ApiOperation(value = "字典类型下拉(分页+条件限制)", notes = "需要登录，显示用户所在应用下（真实应用）的字典类型列表(分页查询)", tags = SwaggerConst.DROP_DOWN_GROUP_NAME)
    public Result<PageResult<BaseDictTypeDropDownResp>> baseDictTypeDropDown(@Validated BaseDictTypeDropDownReq req) {
        return Result.ofSuccess(baseDictManagerService.baseDictTypeDropDown(req));
    }

    @GetMapping("/base-dict/page")
    @ApiOperation(value = "字典明细下拉(分页+条件限制)", notes = "需要登录，显示用户所在应用下（真实应用）的字典明细列表(分页查询)", tags = SwaggerConst.DROP_DOWN_GROUP_NAME)
    public Result<PageResult<BaseDictDropDownResp>> baseDictDropDown(@Validated BaseDictDropDownReq req) {
        return Result.ofSuccess(baseDictManagerService.baseDictDropDown(req));
    }
}
