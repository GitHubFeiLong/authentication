package com.goudong.authentication.server.service.manager;

import com.goudong.authentication.server.rest.req.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * 导入导出管理服务接口
 * @author chenf
 * @version 1.0
 */
public interface ImportExportManagerService {

    /**
     * 导出"resources/templates/"下指定文件（{@code fileName}）
     * @param response 响应对象
     * @param fileName 文件名
     */
    void exportTemplateHandler(HttpServletResponse response, String fileName) throws IOException;

    /**
     * 导入用户
     * @param req 导入参数
     * @return 导入结果
     */
    Boolean importUser(BaseUserImportReq req);

    /**
     * 导出用户
     * @param response 响应
     * @param req 查询参数
     * @return 导出结果
     */
    void exportUser(HttpServletResponse response, BaseUserExportReq req);

    /**
     * 导入角色
     * @param req
     * @return
     */
    Boolean importRole(BaseRoleImportReq req);

    /**
     * 导出角色
     * @param response  响应
     * @param req       导出参数
     */
    void exportRole(HttpServletResponse response, BaseRoleExportReq req);

    /**
     * 导入菜单
     * @param req   导入文件参数
     * @return  true：成功
     */
    Boolean importMenu(BaseMenuImportReq req);


}
