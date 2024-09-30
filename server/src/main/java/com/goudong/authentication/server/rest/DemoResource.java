package com.goudong.authentication.server.rest;

import com.goudong.authentication.server.lang.Result;
import com.goudong.authentication.server.rest.req.BaseAppCreate;
import com.goudong.authentication.server.service.dto.PermissionDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 类描述：
 * 权限资源控制器
 * @author chenf
 * @version 1.0
 */
@RestController
@RequestMapping("/demo")
@Api(tags = "测试")
@Slf4j
public class DemoResource {
    //~fields
    //==================================================================================================================


    //~methods
    //==================================================================================================================
    @PostMapping("/listPermission")
    @ApiOperation(value = "测试", notes = "查询应用的权限列表（资源对应的能访问的角色）")
    public Result<List<PermissionDTO>> demo(HttpServletRequest request, @RequestBody @Validated BaseAppCreate req) {
        System.out.println("req = " + req);
        return Result.ofSuccess(null);
    }

}
