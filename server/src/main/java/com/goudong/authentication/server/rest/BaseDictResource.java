package com.goudong.authentication.server.rest;

import com.goudong.authentication.server.domain.BaseDictType;
import com.goudong.authentication.server.rest.req.*;
import com.goudong.authentication.server.rest.resp.BaseDictPageResp;
import com.goudong.authentication.server.rest.resp.BaseDictTypePageResp;
import com.goudong.authentication.server.rest.resp.BaseRolePageResp;
import com.goudong.authentication.server.rest.resp.BaseRolePermissionListResp;
import com.goudong.authentication.server.service.dto.BaseDictDTO;
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

    @PostMapping("/page/base-dict-types")
    @ApiOperation(value = "分页字典类型")
    public Result<PageResult<BaseDictTypePageResp>> pageBaseDictType(@RequestBody @Validated BaseDictTypePageReq req) {
        return Result.ofSuccess(baseDictManagerService.pageBaseDictType(req));
    }

    @PostMapping("/base-dict-type")
    @ApiOperation(value = "新增字典类型")
    public Result<BaseDictTypeDTO> createBaseDictType(@RequestBody @Validated BaseDictTypeCreateReq req) {
        return Result.ofSuccess(baseDictManagerService.createBaseDictType(req));
    }

    @GetMapping("/base-dict-type/{id}")
    @ApiOperation(value = "查询字典类型")
    public Result<BaseDictTypeDTO> getBaseDictTypeById(@PathVariable Long id) {
        return Result.ofSuccess(baseDictManagerService.getBaseDictTypeById(id));
    }

    @PutMapping("/base-dict-type")
    @ApiOperation(value = "修改字典类型")
    public Result<BaseDictTypeDTO> updateBaseDictType(@RequestBody @Validated BaseDictTypeUpdateReq req) {
        return Result.ofSuccess(baseDictManagerService.updateBaseDictType(req));
    }

    @DeleteMapping("/base-dict-type")
    @ApiOperation(value = "批量删除字典类型")
    public Result<Boolean> deleteBaseDictTypes(@RequestBody @NotNull Long[] ids) {
        return Result.ofSuccess(baseDictManagerService.deleteBaseDictTypes(Arrays.asList(ids)));
    }


    //~BaseDict
    //==================================================================================================================
    @PostMapping("/page/base-dict")
    @ApiOperation(value = "分页字典类型")
    public Result<PageResult<BaseDictPageResp>> pageBaseDict(@RequestBody @Validated BaseDictPageReq req) {
        return Result.ofSuccess(baseDictManagerService.pageBaseDict(req));
    }

    @PostMapping("/base-dict")
    @ApiOperation(value = "新增字典")
    public Result<BaseDictDTO> createBaseDict(@RequestBody @Validated BaseDictCreateReq req) {
        return Result.ofSuccess(baseDictManagerService.createBaseDict(req));
    }

    @GetMapping("/base-dict/{id}")
    @ApiOperation(value = "查询字典类型")
    public Result<BaseDictDTO> getBaseDictById(@PathVariable Long id) {
        return Result.ofSuccess(baseDictManagerService.getBaseDictById(id));
    }

    @PutMapping("/base-dict")
    @ApiOperation(value = "修改字典")
    public Result<BaseDictDTO> updateBaseDict(@RequestBody @Validated BaseDictUpdateReq req) {
        return Result.ofSuccess(baseDictManagerService.updateBaseDict(req));
    }

    @DeleteMapping("/base-dict")
    @ApiOperation(value = "批量删除字典")
    public Result<Boolean> deleteBaseDict(@RequestBody @NotNull Long[] ids) {
        return Result.ofSuccess(baseDictManagerService.deleteBaseDicts(Arrays.asList(ids)));
    }
}
