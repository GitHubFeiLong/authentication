package com.goudong.authentication.server.rest;

import com.goudong.authentication.server.domain.BaseDictType;
import com.goudong.authentication.server.rest.req.*;
import com.goudong.authentication.server.rest.resp.*;
import com.goudong.authentication.server.service.dto.BaseDictDTO;
import com.goudong.authentication.server.service.dto.BaseDictSettingDTO;
import com.goudong.authentication.server.service.dto.BaseDictTypeDTO;
import com.goudong.authentication.server.service.dto.BaseRoleDTO;
import com.goudong.authentication.server.service.manager.BaseDictManagerService;
import com.goudong.authentication.server.service.manager.BaseRoleManagerService;
import com.goudong.core.lang.PageResult;
import com.goudong.core.lang.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Arrays;


/**
 * 类描述：
 * 字典资源控制器
 * @author chenf
 */
@Api(tags = "字典")
@RestController
@RequestMapping("/dict")
public class BaseDictResource {

    //~fields
    //==================================================================================================================
    /**
     * 字典管理接口
     */
    @Resource
    private BaseDictManagerService baseDictManagerService;

    //~methods
    //==================================================================================================================

    //~BaseDictType
    //==================================================================================================================

    @PostMapping("/page/base-dict-type")
    @ApiOperation(value = "【字典类型】分页")
    public Result<PageResult<BaseDictTypePageResp>> pageBaseDictType(@RequestBody @Validated BaseDictTypePageReq req) {
        return Result.ofSuccess(baseDictManagerService.pageBaseDictType(req));
    }

    @PostMapping("/base-dict-type")
    @ApiOperation(value = "【字典类型】新增")
    public Result<BaseDictTypeDTO> createBaseDictType(@RequestBody @Validated BaseDictTypeCreateReq req) {
        return Result.ofSuccess(baseDictManagerService.createBaseDictType(req));
    }

    @GetMapping("/base-dict-type/{id}")
    @ApiOperation(value = "【字典类型】查询")
    public Result<BaseDictTypeDTO> getBaseDictTypeById(@PathVariable Long id) {
        return Result.ofSuccess(baseDictManagerService.getBaseDictTypeById(id));
    }

    @PutMapping("/base-dict-type")
    @ApiOperation(value = "【字典类型】修改")
    public Result<BaseDictTypeDTO> updateBaseDictType(@RequestBody @Validated BaseDictTypeUpdateReq req) {
        return Result.ofSuccess(baseDictManagerService.updateBaseDictType(req));
    }

    @PutMapping("/base-dict-type/change-enabled")
    @ApiOperation(value = "【字典类型】切换激活状态")
    public Result<Boolean> changeEnabledBaseDictType(@RequestBody @Validated BaseDictTypeChangeEnabledReq req) {
        return Result.ofSuccess(baseDictManagerService.changeEnabledBaseDictType(req));
    }

    @DeleteMapping("/base-dict-type")
    @ApiOperation(value = "【字典类型】批量删除")
    public Result<Boolean> deleteBaseDictTypes(@RequestBody @NotNull Long[] ids) {
        return Result.ofSuccess(baseDictManagerService.deleteBaseDictTypes(Arrays.asList(ids)));
    }


    //~BaseDict
    //==================================================================================================================
    @PostMapping("/page/base-dict")
    @ApiOperation(value = "【字典】分页")
    public Result<PageResult<BaseDictPageResp>> pageBaseDict(@RequestBody @Validated BaseDictPageReq req) {
        return Result.ofSuccess(baseDictManagerService.pageBaseDict(req));
    }

    @PostMapping("/base-dict")
    @ApiOperation(value = "【字典】新增")
    public Result<BaseDictDTO> createBaseDict(@RequestBody @Validated BaseDictCreateReq req) {
        return Result.ofSuccess(baseDictManagerService.createBaseDict(req));
    }

    @GetMapping("/base-dict/{id}")
    @ApiOperation(value = "【字典】查询")
    public Result<BaseDictDTO> getBaseDictById(@PathVariable Long id) {
        return Result.ofSuccess(baseDictManagerService.getBaseDictById(id));
    }

    @PutMapping("/base-dict")
    @ApiOperation(value = "【字典】修改")
    public Result<BaseDictDTO> updateBaseDict(@RequestBody @Validated BaseDictUpdateReq req) {
        return Result.ofSuccess(baseDictManagerService.updateBaseDict(req));
    }

    @PutMapping("/base-dict/change-enabled")
    @ApiOperation(value = "【字典】切换激活状态")
    public Result<Boolean> changeEnabledBaseDict(@RequestBody @Validated BaseDictChangeEnabledReq req) {
        return Result.ofSuccess(baseDictManagerService.changeEnabledBaseDict(req));
    }

    @DeleteMapping("/base-dict")
    @ApiOperation(value = "【字典】批量删除")
    public Result<Boolean> deleteBaseDict(@RequestBody @NotNull Long[] ids) {
        return Result.ofSuccess(baseDictManagerService.deleteBaseDicts(Arrays.asList(ids)));
    }

    //~BaseDictSetting
    //==================================================================================================================
    @PostMapping("/page/base-dict-setting")
    @ApiOperation(value = "【字典配置】分页")
    public Result<PageResult<BaseDictSettingPageResp>> pageBaseDictSetting(@RequestBody @Validated BaseDictSettingPageReq req) {
        return Result.ofSuccess(baseDictManagerService.pageBaseDictSetting(req));
    }

    @PostMapping("/base-dict-setting")
    @ApiOperation(value = "【字典配置】新增")
    public Result<BaseDictSettingDTO> createBaseDictSetting(@RequestBody @Validated BaseDictSettingCreateReq req) {
        return Result.ofSuccess(baseDictManagerService.createBaseDictSetting(req));
    }

    @GetMapping("/base-dict-setting/{id}")
    @ApiOperation(value = "【字典配置】查询")
    public Result<BaseDictSettingDTO> getBaseDictSettingById(@PathVariable Long id) {
        return Result.ofSuccess(baseDictManagerService.getBaseDictSettingById(id));
    }

    @PutMapping("/base-dict-setting")
    @ApiOperation(value = "【字典配置】修改")
    public Result<BaseDictSettingDTO> updateBaseDictSetting(@RequestBody @Validated BaseDictSettingUpdateReq req) {
        return Result.ofSuccess(baseDictManagerService.updateBaseDictSetting(req));
    }

    @PutMapping("/base-dict-setting/change-enabled")
    @ApiOperation(value = "【字典配置】切换激活状态")
    public Result<Boolean> changeEnabledBaseDictSetting(@RequestBody @Validated BaseDictSettingChangeEnabledReq req) {
        return Result.ofSuccess(baseDictManagerService.changeEnabledBaseDictSetting(req));
    }

    @DeleteMapping("/base-dict-setting")
    @ApiOperation(value = "批量删除字典")
    public Result<Boolean> deleteBaseDictSettings(@RequestBody @NotNull Long[] ids) {
        return Result.ofSuccess(baseDictManagerService.deleteBaseDictSettings(Arrays.asList(ids)));
    }
}
