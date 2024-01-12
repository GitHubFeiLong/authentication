package com.goudong.authentication.server.rest;

import com.goudong.authentication.server.constant.RoleConst;
import com.goudong.authentication.server.rest.req.BaseAppCertCreateReq;
import com.goudong.authentication.server.rest.req.BaseAppCreate;
import com.goudong.authentication.server.rest.req.BaseAppUpdate;
import com.goudong.authentication.server.rest.req.search.BaseAppPageReq;
import com.goudong.authentication.server.rest.resp.BaseAppPageResp;
import com.goudong.authentication.server.service.dto.BaseAppCertDTO;
import com.goudong.authentication.server.service.dto.BaseAppDTO;
import com.goudong.authentication.server.service.manager.BaseAppManagerService;
import com.goudong.core.lang.PageResult;
import com.goudong.core.lang.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 类描述：
 * 应用管理
 * @ClassName BaseAppResource
 * @Description 新增、删除、修改、分页、下拉
 * @Author Administrator
 * @Date 2023/7/29 11:33
 * @Version 1.0
 */
@RestController
@RequestMapping("/app")
@Api(tags = "应用管理")
@Secured(value = RoleConst.ROLE_APP_SUPER_ADMIN) // 只有该角色才能处理应用
public class BaseAppResource {

    //~fields
    //==================================================================================================================
    @Resource
    private BaseAppManagerService baseAppManagerService;

    //~methods
    //==================================================================================================================
    @PostMapping("/page/base-apps")
    @ApiOperation("分页查询应用")
    public Result<PageResult<BaseAppPageResp>> page(@RequestBody @Validated BaseAppPageReq req) {
        return Result.ofSuccess(baseAppManagerService.page(req));
    }

    @PostMapping("/base-app")
    @ApiOperation("新增应用")
    public Result<BaseAppDTO> create(@Valid @RequestBody BaseAppCreate req) {
        BaseAppDTO result = baseAppManagerService.save(req);
        return Result.ofSuccess(result);
    }

    @PutMapping("/base-app")
    @ApiOperation("修改应用")
    public Result<BaseAppDTO> update(@Valid @RequestBody BaseAppUpdate req) {
        BaseAppDTO result = baseAppManagerService.update(req);
        return Result.ofSuccess(result);
    }

    @DeleteMapping("/base-app/{id}")
    @ApiOperation("删除应用")
    public Result<Boolean> delete(@PathVariable Long id) {
        baseAppManagerService.deleteById(id);
        return Result.ofSuccess(true);
    }

    @GetMapping("/base-app-certs/{appId}")
    @ApiOperation("应用证书列表")
    public Result<List<BaseAppCertDTO>> listCertsByAppId(@PathVariable Long appId) {
        return Result.ofSuccess(baseAppManagerService.listCertsByAppId(appId));
    }

    @PostMapping("/base-app-cert")
    @ApiOperation("新增证书")
    public Result<BaseAppCertDTO> createCert(@RequestBody @Validated BaseAppCertCreateReq req) {
        return Result.ofSuccess(baseAppManagerService.createCert(req));
    }
}