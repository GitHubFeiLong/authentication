package com.goudong.authentication.server.rest;

import com.goudong.authentication.common.constant.CommonConst;
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
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 类描述：
 * 应用管理
 * @author chenf
 */
@RestController
@RequestMapping("/app")
@Api(tags = "应用管理")
// @Secured(value = CommonConst.ROLE_APP_SUPER_ADMIN) // 只有该角色才能处理应用
public class BaseAppResource {

    //~fields
    //==================================================================================================================
    /**
     * 应用管理服务层接口
     */
    @Resource
    private BaseAppManagerService baseAppManagerService;

    //~methods
    //==================================================================================================================

    /**
     * 分页查询应用
     * @param req 分页参数
     * @return 分页应用
     */
    @PostMapping("/page/base-apps")
    @ApiOperation("分页查询应用")
    public Result<PageResult<BaseAppPageResp>> page(@RequestBody @Validated BaseAppPageReq req) {
        return Result.ofSuccess(baseAppManagerService.page(req));
    }

    /**
     * 新增应用
     * @param req   新增应用参数
     * @return  新增应用
     */
    @PostMapping("/base-app")
    @ApiOperation("新增应用")
    public Result<BaseAppDTO> create(@Valid @RequestBody BaseAppCreate req) {
        BaseAppDTO result = baseAppManagerService.save(req);
        return Result.ofSuccess(result);
    }

    /**
     * 修改应用
     * @param req   修改应用参数
     * @return  修改后应用
     */
    @PutMapping("/base-app")
    @ApiOperation("修改应用")
    public Result<BaseAppDTO> update(@Valid @RequestBody BaseAppUpdate req) {
        BaseAppDTO result = baseAppManagerService.update(req);
        return Result.ofSuccess(result);
    }

    /**
     * 删除应用
     * @param id    被删除的应用id
     * @deprecated  使用批量删除接口
     * @return  true：删除成功；false：删除失败
     */
    @Deprecated
    // @DeleteMapping("/base-app/{id}")
    @ApiOperation(value = "删除应用", hidden = true)
    public Result<Boolean> delete(@PathVariable Long id) {
        baseAppManagerService.deleteById(id);
        return Result.ofSuccess(true);
    }

    /**
     * 删除应用
     * @param ids    被删除的应用id
     * @return  true：删除成功；false：删除失败
     */
    @DeleteMapping("/base-apps")
    @ApiOperation("批量删除应用")
    public Result<Boolean> deleteByIds(@RequestBody @NotNull Long[] ids) {
        baseAppManagerService.deleteByIds(ids);
        return Result.ofSuccess(true);
    }

    /**
     * 查询应用证书列表
     * @param appId 应用id
     * @return  应用所有证书
     */
    @GetMapping("/base-app-certs/{appId}")
    @ApiOperation("应用证书列表")
    public Result<List<BaseAppCertDTO>> listCertsByAppId(@PathVariable Long appId) {
        return Result.ofSuccess(baseAppManagerService.listCertsByAppId(appId));
    }

    /**
     * 新增应用证书
     * @param req   新增应用证书参数
     * @return  新增证书信息
     */
    @PostMapping("/base-app-cert")
    @ApiOperation("新增证书")
    public Result<BaseAppCertDTO> createCert(@RequestBody @Validated BaseAppCertCreateReq req) {
        return Result.ofSuccess(baseAppManagerService.createCert(req));
    }
}
