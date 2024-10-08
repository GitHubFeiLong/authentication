package com.goudong.authentication.server.rest;

import com.goudong.authentication.common.constant.CommonConst;
import com.goudong.authentication.common.core.Token;
import com.goudong.authentication.common.core.UserDetail;
import com.goudong.authentication.server.lang.PageResult;
import com.goudong.authentication.server.lang.Result;
import com.goudong.authentication.server.rest.req.*;
import com.goudong.authentication.server.rest.resp.BaseUserPageResp;
import com.goudong.authentication.server.service.dto.BaseUserDTO;
import com.goudong.authentication.server.service.manager.BaseUserManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Arrays;


/**
 * 类描述：
 * 用户资源控制器
 * @author chenf
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户")
@Slf4j
public class BaseUserResource {

    //~fields
    //==================================================================================================================
    /**
     * 用户管理服务层接口
     */
    @Resource
    private BaseUserManagerService baseUserManagerService;

    //~methods
    //==================================================================================================================

    /**
     * 登录接口
     * @return  用户token信息
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录(password)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用id", required = false),
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true),
    })
    public Result<Token> login() {
        return Result.ofSuccess(new Token());
    }

    /**
     * 刷新token
     * @param token 刷新令牌
     * @return      用户新token信息
     */
    @PostMapping("/refresh-token")
    @ApiOperation(value = "刷新token")
    public Result<Token> refreshToken(@RequestBody BaseUserRefreshTokenReq token) {
        return Result.ofSuccess(baseUserManagerService.refreshToken(token));
    }

    /**
     * 注销登录
     */
    @PutMapping("/logout")
    @ApiOperation(value = "注销")
    public Result<Object> logout() {
        return Result.ofSuccess();
    }

    /**
     * 根据token获取用户信息
     * @param token 令牌
     * @return      token用户的详细信息
     */
    @GetMapping("/base-user/token/detail/{token}")
    @ApiOperation(value = "查询用户信息(token)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
    })
    public Result<UserDetail> getUserDetailByToken(@PathVariable String token) {
        return Result.ofSuccess(baseUserManagerService.getUserDetailByToken(token));
    }

    /**
     * 根据token获取用户信息，post方法可以避免在不同浏览器使用时，超过url的长度限制
     * @param req   令牌相关参数
     * @return      token用户的详细信息
     */
    @PostMapping("/base-user/token/detail")
    @ApiOperation(value = "查询用户信息(token)")
    public Result<UserDetail> getUserDetailByToken(@RequestBody @Validated BaseUserGetUserDetailByTokenReq req) {
        return Result.ofSuccess(baseUserManagerService.getUserDetailByToken(req.getToken()));
    }

    /**
     * 分页查询用户列表
     * @param req   分页参数
     * @return      分页用户列表
     */
    @PostMapping("/page/base-users")
    @ApiOperation(value = "分页用户")
    public Result<PageResult<BaseUserPageResp>> page(@RequestBody @Validated BaseUserPageReq req) {
        return Result.ofSuccess(baseUserManagerService.page(req));
    }

    /**
     * 添加用户
     * @param req   添加用户参数
     * @return      新建用户信息
     */
    @PostMapping("/base-user/simple-create")
    @ApiOperation(value = "简单新增用户")
    public Result<BaseUserDTO> simpleCreateUser(@RequestBody @Validated BaseUserSimpleCreateReq req) {
        return Result.ofSuccess(baseUserManagerService.simpleCreateUser(req));
    }

    /**
     * 修改用户
     * @param req   修改用户
     * @return      修改后用户信息
     */
    @PutMapping("/base-user/simple-update")
    @ApiOperation(value = "修改用户")
    public Result<BaseUserDTO> update(@RequestBody @Validated BaseUserSimpleUpdateReq req) {
        return Result.ofSuccess(baseUserManagerService.simpleUpdateUser(req));
    }

    /**
     * 重置密码
     * @param userId    用户id
     * @return          true：修改成功；false：修改失败
     */
    @PutMapping("/base-user/reset-password/{userId}")
    @ApiOperation(value = "重置密码")
    public Result<Boolean> resetPassword(@PathVariable Long userId) {
        return Result.ofSuccess(baseUserManagerService.resetPassword(userId));
    }

    /**
     * 修改激活状态
     * @param userId    用户id
     * @return          true：修改成功；false：修改失败
     */
    @PutMapping("/base-user/change-enabled/{userId}")
    @ApiOperation(value = "修改激活状态")
    public Result<Boolean> changeEnabled(@PathVariable Long userId) {
        return Result.ofSuccess(baseUserManagerService.changeEnabled(userId));
    }

    /**
     * 修改锁定状态
     * @param userId    用户id
     * @return          true：修改成功；false：修改失败
     */
    @PutMapping("/base-user/change-locked/{userId}")
    @ApiOperation(value = "修改锁定状态")
    public Result<Boolean> changeLocked(@PathVariable Long userId) {
        return Result.ofSuccess(baseUserManagerService.changeLocked(userId));
    }

    /**
     * 批量删除用户
     * @param ids       用户id数组
     * @return          true：删除成功；false：删除失败
     */
    @DeleteMapping("/base-users")
    @ApiOperation(value = "批量删除用户")
    public Result<Boolean> deleteByIds(@RequestBody @NotNull Long[] ids) {
        return Result.ofSuccess(baseUserManagerService.deleteByIds(Arrays.asList(ids)));
    }

    @PostMapping("/supplement-token")
    @ApiOperation(value = "补充token")
    public Result<Token> supplementToken(@RequestBody BaseUserSupplementTokenReq req) {
        return Result.ofSuccess(baseUserManagerService.supplementToken(req));
    }

    /**
     * 修改用户密码
     * @param req 修改用户密码参数
     * @return true：修改成功；false：修改失败
     */
    @PutMapping("/base-user/change-password")
    @ApiOperation(value = "修改用户密码")
    public Result<Boolean> changePassword(@RequestBody @Validated BaseUserChangePasswordReq req) {
        return Result.ofSuccess(baseUserManagerService.changePassword(req));
    }

    @PostMapping("/create-token")
    @ApiOperation(value = "给指定用户创建令牌")
    @Secured(value = {CommonConst.ROLE_APP_SUPER_ADMIN, CommonConst.ROLE_APP_ADMIN}) // 只有该角色才能处理应用
    public Result<Token> createToken(@RequestBody BaseUserCreateTokenReq req) {
        return Result.ofSuccess(baseUserManagerService.createToken(req));
    }
}
