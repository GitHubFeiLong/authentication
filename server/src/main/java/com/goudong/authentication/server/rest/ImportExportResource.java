package com.goudong.authentication.server.rest;

import com.goudong.authentication.server.lang.Result;
import com.goudong.authentication.server.rest.req.*;
import com.goudong.authentication.server.service.manager.ImportExportManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * 导入导出
 * @author chenf
 */

@Api(tags = "导入导出接口")
@Controller
@RequestMapping("/import-export")
public class ImportExportResource {
    //~fields
    //==================================================================================================================
    @Resource
    private ImportExportManagerService importExportManagerService;

    //~methods
    //==================================================================================================================

    @GetMapping("/export-user-template")
    @ApiOperation("用户模板导出")
    public void exportUserTemplate(HttpServletResponse response) throws IOException {
        importExportManagerService.exportTemplateHandler(response, "template-user.xlsx");
    }

    @PostMapping("/import-user")
    @ApiOperation("用户导入")
    @ResponseBody
    public Result<Boolean> importUser(@Validated BaseUserImportReq req) {
        return Result.ofSuccess(importExportManagerService.importUser(req));
    }

    @PostMapping("/export-user")
    @ApiOperation("用户导出")
    public void exportUser(HttpServletResponse response, @RequestBody BaseUserExportReq req) {
        importExportManagerService.exportUser(response, req);
    }

    @GetMapping("/export-role-template")
    @ApiOperation("角色模板导出")
    public void exportRoleTemplate(HttpServletResponse response) throws IOException {
        importExportManagerService.exportTemplateHandler(response, "template-role.xlsx");
    }

    @PostMapping("/import-role")
    @ApiOperation("角色导入")
    @ResponseBody
    public Result<Boolean> importRole(@Validated BaseRoleImportReq req) {
        return Result.ofSuccess(importExportManagerService.importRole(req));
    }

    @PostMapping("/export-role")
    @ApiOperation("角色导出")
    public void exportRole(HttpServletResponse response, @RequestBody BaseRoleExportReq req) {
        importExportManagerService.exportRole(response, req);
    }

    @GetMapping("/export-menu-template")
    @ApiOperation("菜单模板导出")
    public void exportMenuTemplate(HttpServletResponse response) throws IOException {
        importExportManagerService.exportTemplateHandler(response, "template-menu.xlsx");
    }

    @PostMapping("/import-menu")
    @ApiOperation("菜单导入")
    @ResponseBody
    public Result<Boolean> importMenu(@Validated BaseMenuImportReq req) {
        return Result.ofSuccess(importExportManagerService.importMenu(req));
    }

    @PostMapping("/export-menu")
    @ApiOperation("菜单导出")
    public void exportMenu(HttpServletResponse response, @RequestBody BaseMenuExportReq req) {
        importExportManagerService.exportMenu(response, req);
    }

    @GetMapping("/export-app-template")
    @ApiOperation("应用模板导出")
    public void exportAppTemplate(HttpServletResponse response) throws IOException {
        importExportManagerService.exportTemplateHandler(response, "template-app.xlsx");
    }

    @PostMapping("/import-app")
    @ApiOperation("应用导入")
    @ResponseBody
    public Result<Boolean> importApp(@Validated BaseAppImportReq req) {
        return Result.ofSuccess(importExportManagerService.importApp(req));
    }

    @PostMapping("/export-app")
    @ApiOperation("应用导出")
    public void exportApp(HttpServletResponse response, @RequestBody BaseAppExportReq req) {
        importExportManagerService.exportApp(response, req);
    }

    @GetMapping("/export-dict-type-template")
    @ApiOperation("字典类型模板导出")
    public void exportDictTypeTemplate(HttpServletResponse response) throws IOException {
        importExportManagerService.exportTemplateHandler(response, "template-dict-type.xlsx");
    }

    @PostMapping("/import-dict-type")
    @ApiOperation("字典类型导入")
    @ResponseBody
    public Result<Boolean> importDictType(@Validated BaseDictTypeImportReq req) {
        return Result.ofSuccess(importExportManagerService.importDictType(req));
    }

    @PostMapping("/export-dict-type")
    @ApiOperation("字典类型模板导出")
    public void exportDictType(HttpServletResponse response, @RequestBody BaseDictTypeExportReq req) {
        importExportManagerService.exportDictType(response, req);
    }

    @GetMapping("/export-dict-template")
    @ApiOperation("字典明细模板导出")
    public void exportDictTemplate(HttpServletResponse response) throws IOException {
        importExportManagerService.exportTemplateHandler(response, "template-dict.xlsx");
    }

    @PostMapping("/import-dict")
    @ApiOperation("字典明细导入")
    @ResponseBody
    public Result<Boolean> importDict(@Validated BaseDictImportReq req) {
        return Result.ofSuccess(importExportManagerService.importDict(req));
    }

    @PostMapping("/export-dict")
    @ApiOperation("字典明细导出")
    public void exportDict(HttpServletResponse response, @RequestBody BaseDictExportReq req) {
        importExportManagerService.exportDict(response, req);
    }

    @GetMapping("/export-dict-setting-template")
    @ApiOperation("字典明细模板导出")
    public void exportDictSettingTemplate(HttpServletResponse response) throws IOException {
        importExportManagerService.exportTemplateHandler(response, "template-dict-setting.xlsx");
    }

    @PostMapping("/import-dict-setting")
    @ApiOperation("字典明细导入")
    @ResponseBody
    public Result<Boolean> importDictSetting(@Validated BaseDictSettingImportReq req) {
        return Result.ofSuccess(importExportManagerService.importDictSetting(req));
    }

    @PostMapping("/export-dict-setting")
    @ApiOperation("字典配置导出")
    public void exportDictSetting(HttpServletResponse response, @RequestBody BaseDictSettingExportReq req) {
        importExportManagerService.exportDictSetting(response, req);
    }
}
