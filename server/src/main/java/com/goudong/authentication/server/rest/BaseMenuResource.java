package com.goudong.authentication.server.rest;

import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.rest.req.BaseMenuChangeSortNumReq;
import com.goudong.authentication.server.rest.req.BaseMenuCreateReq;
import com.goudong.authentication.server.rest.req.BaseMenuGetAllReq;
import com.goudong.authentication.server.rest.req.BaseMenuUpdateReq;
import com.goudong.authentication.server.rest.resp.BaseMenuGetAllResp;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.authentication.server.service.manager.BaseMenuManagerService;
import com.goudong.core.lang.Result;
import com.goudong.core.util.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * 类描述：
 * 菜单资源控制器
 * @author chenf
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/menu")
public class BaseMenuResource {
    //~fields
    //==================================================================================================================
    @Resource
    private BaseMenuManagerService baseMenuManagerService;

    //~methods
    //==================================================================================================================

    @PostMapping("/base-menus")
    @ApiOperation(value = "查询所有菜单")
    public Result<BaseMenuGetAllResp> getAll(@RequestBody @Validated BaseMenuGetAllReq req) {
        return Result.ofSuccess(baseMenuManagerService.getAll(req));
    }

    @PostMapping("/base-menu")
    @ApiOperation(value = "新增菜单")
    public Result<BaseMenuDTO> save(@RequestBody @Validated BaseMenuCreateReq req) {
        return Result.ofSuccess(baseMenuManagerService.save(req));
    }

    @PutMapping("/base-menu")
    @ApiOperation(value = "修改菜单")
    public Result<BaseMenuDTO> update(@RequestBody @Validated BaseMenuUpdateReq req) {
        return Result.ofSuccess(baseMenuManagerService.update(req));
    }

    @PutMapping("/base-menu/sort-num")
    @ApiOperation(value = "修改菜单排序")
    public Result<Boolean> changeSortNum(@RequestBody @Validated BaseMenuChangeSortNumReq req) {
        return Result.ofSuccess(baseMenuManagerService.changeSortNum(req));
    }

    /**
     * 根据id删除菜单
     * @param id    菜单id
     * @deprecated  使用批量删除接口
     * @return
     */
    @Deprecated
    // @DeleteMapping("/base-menu/{id}")
    @ApiOperation(value = "删除菜单", notes = "如果是父节点，那么就会删除它及它下面的所有子节点", hidden = true)
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ofSuccess(baseMenuManagerService.deleteById(id));
    }

    /**
     * 删除菜单
     * @param ids    被删除的应用id集合
     * @return  true：删除成功；false：删除失败
     */
    @DeleteMapping("/base-menus")
    @ApiOperation("批量删除菜单")
    public Result<Boolean> deleteByIds(@RequestBody @NotNull Long[] ids) {
        baseMenuManagerService.deleteByIds(ListUtil.newArrayList(ids));
        return Result.ofSuccess(true);
    }

}
