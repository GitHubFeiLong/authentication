package com.goudong.authentication.server.service.manager.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.authentication.common.util.JsonUtil;
import com.goudong.authentication.server.constant.CommonConst;
import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.easyexcel.listener.BaseAppImportExcelListener;
import com.goudong.authentication.server.easyexcel.listener.BaseMenuImportExcelListener;
import com.goudong.authentication.server.easyexcel.listener.BaseRoleImportExcelListener;
import com.goudong.authentication.server.easyexcel.listener.BaseUserImportExcelListener;
import com.goudong.authentication.server.easyexcel.template.*;
import com.goudong.authentication.server.enums.option.ActivateEnum;
import com.goudong.authentication.server.enums.option.LockEnum;
import com.goudong.authentication.server.enums.option.MenuTypeEnum;
import com.goudong.authentication.server.properties.AuthenticationServerProperties;
import com.goudong.authentication.server.rest.req.*;
import com.goudong.authentication.server.rest.req.search.BaseAppPageReq;
import com.goudong.authentication.server.rest.resp.BaseAppPageResp;
import com.goudong.authentication.server.rest.resp.BaseRoleDropDownResp;
import com.goudong.authentication.server.rest.resp.BaseRolePageResp;
import com.goudong.authentication.server.rest.resp.BaseUserPageResp;
import com.goudong.authentication.server.service.BaseMenuService;
import com.goudong.authentication.server.service.BaseRoleService;
import com.goudong.authentication.server.service.BaseUserService;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseAppManagerService;
import com.goudong.authentication.server.service.manager.ImportExportManagerService;
import com.goudong.authentication.server.service.mapper.BaseMenuMapper;
import com.goudong.authentication.server.util.SecurityContextUtil;
import com.goudong.core.lang.IEnum;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.CollectionUtil;
import com.goudong.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 导入导出管理服务接口实现
 * @author chenf
 * @version 1.0
 */
@Slf4j
@Service
public class ImportExportManagerServiceImpl implements ImportExportManagerService {

    //~fields
    //==================================================================================================================
    /**
     * 模板的目录前缀
     */
    public static final String PREFIX_DIR = "templates/";

    /**
     * 事务模板
     */
    @Resource
    private TransactionTemplate transactionTemplate;

    /**
     * 用户服务接口
     */
    @Resource
    private BaseUserService baseUserService;

    /**
     * 角色服务接口
     */
    @Resource
    private BaseRoleService baseRoleService;

    /**
     * 菜单服务接口
     */
    @Resource
    private BaseMenuService baseMenuService;

    /**
     * 密码编码器
     */
    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 菜单对象映射器
     */
    @Resource
    private BaseMenuMapper baseMenuMapper;

    /**
     * 认证服务配置
     */
    @Resource
    private AuthenticationServerProperties authenticationServerProperties;

    /**
     * 菜单对象映射器
     */
    @Resource
    private BaseAppManagerService baseAppManagerService;

    @Resource
    private ObjectMapper objectMapper;

    //~methods
    //==================================================================================================================
    /**
     * 导出"resources/templates/"下指定文件（{@code fileName}）
     * @param response 响应对象
     * @param fileName 文件名
     */
    public void exportTemplateHandler(HttpServletResponse response, String fileName) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream fileInputStream = loader.getResourceAsStream(PREFIX_DIR + fileName);

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        byte[] buffer = new byte[1024];
        int len;
        //4、执行写出操作
        while ((len = fileInputStream.read(buffer)) != -1){
            outputStream.write(buffer,0,len);
        }

        // 关闭输入流和输出流
        fileInputStream.close();
        outputStream.close();
    }

    /**
     * 导入用户
     *
     * @param req 导入参数
     * @return 导入结果
     */
    @Override
    public Boolean importUser(BaseUserImportReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        try {
            EasyExcel.read(req.getFile().getInputStream(), BaseUserImportExcelTemplate.class,
                    new BaseUserImportExcelListener(
                            authenticationServerProperties,
                            myAuthentication,
                            baseUserService,
                            baseRoleService,
                            transactionTemplate,
                            passwordEncoder))
                    .sheet()
                    // 第二行开始解析
                    .headRowNumber(2)
                    .doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * 导出用户
     *
     * @param response 响应
     * @param req      查询参数
     * @return 导出结果
     */
    @Override
    public void exportUser(HttpServletResponse response, BaseUserExportReq req) {
        log.info("开始执行用户导出");
        String fileName = "导出用户" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");

        // 这里 需要指定写用哪个class去写
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), BaseUserExportTemplate.class)
                .registerConverter(new LongStringConverter())
                .build()) {
            // 这里注意 如果同一个sheet只要创建一次
            WriteSheet writeSheet = EasyExcel.writerSheet("sheet01").build();
            /*
                分页写入excel，对内存友好
             */
            // 分页查询
            BaseUserPageReq pageReq = req.getPageReq();
            // 每次查询100条
            pageReq.setSize(100);
            pageReq.setIds(req.getIds());
            // 初始分页设置0
            int page = 0;
            // 总店铺数量
            long total = 0;
            AtomicInteger sequenceNumber = new AtomicInteger(1);
            do {
                List<BaseUserExportTemplate> result = new ArrayList<>();
                // 页码加一
                page += 1;
                pageReq.setPage(page);
                // 分页查询(展开的)
                PageResult<BaseUserPageResp> pageResult = baseUserService.page(pageReq);
                // 获取总数量
                total = pageResult.getTotal();

                // 将数据放入resule中
                pageResult.getContent().forEach(p -> {
                    BaseUserExportTemplate resp = new BaseUserExportTemplate();
                    resp.setSequenceNumber(sequenceNumber.getAndIncrement());
                    resp.setUsername(p.getUsername());
                    resp.setRoles(p.getRoles().stream().map(BaseRoleDropDownResp::getName).collect(Collectors.joining(CommonConst.COMMA)));
                    resp.setValidTime(p.getValidTime());
                    resp.setEnabled(IEnum.getById(ActivateEnum.class, p.getEnabled()).getLabel());
                    resp.setLocked(IEnum.getById(LockEnum.class, p.getLocked()).getLabel());
                    resp.setRemark(p.getRemark());
                    resp.setCreatedDate(p.getCreatedDate());
                    result.add(resp);
                });

                // 调用写入
                excelWriter.write(result, writeSheet);
            } while (((long) page * pageReq.getSize()) < total);

        } catch (IOException e) {
            throw new RuntimeException("导出用户失败：" + e.getMessage(), e);
        }
    }

    /**
     * 导出角色
     *
     * @param req
     * @return
     */
    @Override
    public Boolean importRole(BaseRoleImportReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        try {
            EasyExcel.read(req.getFile().getInputStream(), BaseRoleImportExcelTemplate.class,
                            new BaseRoleImportExcelListener(myAuthentication, baseRoleService, transactionTemplate))
                    .sheet()
                    // 第二行开始解析
                    .headRowNumber(2)
                    .doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * 导出角色
     * @param response  响应
     * @param req       导出参数
     */
    @Override
    public void exportRole(HttpServletResponse response, BaseRoleExportReq req) {
        log.info("开始执行角色导出");
        String fileName = "导出角色" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");

        // 这里 需要指定写用哪个class去写
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), BaseRoleExportTemplate.class)
                .registerConverter(new LongStringConverter())
                .build()) {
            // 这里注意 如果同一个sheet只要创建一次
            WriteSheet writeSheet = EasyExcel.writerSheet("sheet01").build();
            /*
                分页写入excel，对内存友好
             */
            // 分页查询
            BaseRolePageReq pageReq = req.getPageReq();
            // 每次查询100条
            pageReq.setSize(100);
            // 优先使用表格勾选的记录作为条件
            pageReq.setIds(CollectionUtil.isNotEmpty(req.getIds()) ? req.getIds() : pageReq.getIds());
            // 初始分页设置0
            int page = 0;
            // 总店铺数量
            long total = 0;
            AtomicInteger sequenceNumber = new AtomicInteger(1);
            do {
                List<BaseRoleExportTemplate> result = new ArrayList<>();
                // 页码加一
                page += 1;
                pageReq.setPage(page);
                // 分页查询(展开的)
                PageResult<BaseRolePageResp> pageResult = baseRoleService.page(pageReq);
                // 获取总数量
                total = pageResult.getTotal();

                // 将数据放入resule中
                pageResult.getContent().forEach(p -> {
                    BaseRoleExportTemplate resp = new BaseRoleExportTemplate();
                    resp.setSequenceNumber(sequenceNumber.getAndIncrement());
                    resp.setName(p.getName());
                    resp.setUserNumber(p.getUsers().size());
                    resp.setRemark(p.getRemark());
                    resp.setCreatedDate(p.getCreatedDate());
                    result.add(resp);
                });

                // 调用写入
                excelWriter.write(result, writeSheet);
            } while (((long) page * pageReq.getSize()) < total);

        } catch (IOException e) {
            throw new RuntimeException("导出角色失败：" + e.getMessage(), e);
        }
    }

    /**
     * 导入菜单
     * @param req   导入文件参数
     * @return  true：成功
     */
    @Override
    public Boolean importMenu(BaseMenuImportReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        try {
            EasyExcel.read(req.getFile().getInputStream(), BaseMenuImportExcelTemplate.class,
                            new BaseMenuImportExcelListener(myAuthentication, baseMenuService, baseRoleService, transactionTemplate, baseMenuMapper))
                    .sheet()
                    // 第二行开始解析
                    .headRowNumber(2)
                    .doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * 导出菜单
     * @param response  响应
     * @param req       导出参数
     */
    @Override
    public void exportMenu(HttpServletResponse response, BaseMenuExportReq req) {
        log.info("开始执行菜单导出");
        String fileName = "导出菜单" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");

        // 这里 需要指定写用哪个class去写
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), BaseMenuExportTemplate.class)
                .registerConverter(new LongStringConverter())
                .build()) {
            // 这里注意 如果同一个sheet只要创建一次
            WriteSheet writeSheet = EasyExcel.writerSheet("sheet01").build();
            MyAuthentication myAuthentication = SecurityContextUtil.get();
            Long appId = myAuthentication.getRealAppId();
            BaseMenuGetAllReq pageReq = req.getPageReq();
            pageReq.setIds(req.getIds());
            pageReq.setAppId(appId);
            // 查询菜单
            List<BaseMenuDTO> menus = baseMenuService.findAll(pageReq);
            // 转map
            Map<Long, BaseMenuDTO> longBaseMenuDTOMap = menus.stream().collect(Collectors.toMap(BaseMenuDTO::getId, p -> p, (k1, k2) -> k1));

            Map<Long, BaseMenu> parentMenuMap = new HashMap<>();
            AtomicInteger sequenceNumber = new AtomicInteger(1);
            List<BaseMenuExportTemplate> result = new ArrayList<>();
            menus.forEach(p -> {
                BaseMenuExportTemplate resp = new BaseMenuExportTemplate();
                resp.setSequenceNumber(sequenceNumber.getAndIncrement());
                resp.setPermissionId(p.getPermissionId());
                if (p.getParentId() != null) {
                    if (longBaseMenuDTOMap.containsKey(p.getParentId())) {
                        BaseMenuDTO parentMenuDTO = longBaseMenuDTOMap.get(p.getParentId());
                        resp.setParentPermissionId(parentMenuDTO.getPermissionId());
                    } else {
                        if (parentMenuMap.containsKey(p.getParentId())) {
                            BaseMenu baseMenu = parentMenuMap.get(p.getParentId());
                            if (baseMenu != null) {
                                resp.setParentPermissionId(baseMenu.getPermissionId());
                            }
                        } else {
                            BaseMenu baseMenu = baseMenuService.findById(p.getParentId());
                            parentMenuMap.put(p.getParentId(), baseMenu);
                            if (baseMenu != null) {
                                resp.setParentPermissionId(baseMenu.getPermissionId());
                            }
                        }
                    }
                }
                if (StringUtil.isNotBlank(p.getMethod())) {
                    List<String> methods = JsonUtil.toList(p.getMethod(), String.class);
                    resp.setMethod(String.join(CommonConst.COMMA, methods));
                }

                resp.setName(p.getName());
                resp.setType(MenuTypeEnum.MENU.getById(p.getType()).getLabel());
                resp.setPath(p.getPath());
                resp.setMeta(p.getMeta());
                resp.setRemark(p.getRemark());
                result.add(resp);
            });
            // 调用写入
            excelWriter.write(result, writeSheet);
        } catch (IOException e) {
            throw new RuntimeException("导出菜单失败：" + e.getMessage(), e);
        }
    }

    /**
     * 导入应用
     *
     * @param req 导入参数
     * @return true 成功
     */
    @Override
    public Boolean importApp(BaseAppImportReq req) {
        try {
            EasyExcel.read(req.getFile().getInputStream(), BaseAppImportExcelTemplate.class,
                            new BaseAppImportExcelListener(baseAppManagerService, transactionTemplate))
                    .sheet()
                    // 第二行开始解析
                    .headRowNumber(2)
                    .doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * 导出应用
     *
     * @param response 响应对象
     * @param req      导出参数
     */
    @Override
    public void exportApp(HttpServletResponse response, BaseAppExportReq req) {
        log.info("开始执行应用导出");
        String fileName = "导出应用" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");

        // 这里 需要指定写用哪个class去写
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), BaseAppExportTemplate.class)
                .registerConverter(new LongStringConverter())
                .build()) {
            // 这里注意 如果同一个sheet只要创建一次
            WriteSheet writeSheet = EasyExcel.writerSheet("sheet01").build();
            /*
                分页写入excel，对内存友好
             */
            // 分页查询
            BaseAppPageReq pageReq = req.getPageReq();
            // 每次查询100条
            pageReq.setSize(100);
            pageReq.setIds(req.getIds());
            // 初始分页设置0
            int page = 0;
            // 总店铺数量
            long total = 0;
            AtomicInteger sequenceNumber = new AtomicInteger(1);
            do {
                List<BaseAppExportTemplate> result = new ArrayList<>();
                // 页码加一
                page += 1;
                pageReq.setPage(page);
                // 分页查询(展开的)
                PageResult<BaseAppPageResp> pageResult = baseAppManagerService.page(pageReq);
                // 获取总数量
                total = pageResult.getTotal();

                // 将数据放入resule中
                pageResult.getContent().forEach(p -> {
                    BaseAppExportTemplate resp = new BaseAppExportTemplate();
                    resp.setSequenceNumber(sequenceNumber.getAndIncrement());
                    resp.setId(p.getId());
                    resp.setSecret(p.getSecret());
                    resp.setName(p.getName());
                    resp.setHomePage(p.getHomePage());
                    resp.setEnableStatus(ActivateEnum.ACTIVATED.getById(p.getEnabled()).getLabel());
                    resp.setRemark(p.getRemark());
                    resp.setCreatedDate(p.getCreatedDate());

                    result.add(resp);
                });

                // 调用写入
                excelWriter.write(result, writeSheet);
            } while (((long) page * pageReq.getSize()) < total);

        } catch (IOException e) {
            throw new RuntimeException("导出应用失败：" + e.getMessage(), e);
        }
    }
}
