import request from "@/utils/request";
import {exportExcel} from "@/utils/export";
import {API_PREFIX} from "@/constant/commons";

/**
 * 上传单文件
 * @param file
 * @returns {*}
 */
export function simpleUpload(file) {
  return request({
    url: `/api/file/upload-group/upload`,
    method: 'post',
    data: file
  })
}

// ~ 用户管理
// =====================================================================================================================
/**
 * 导出用户模板
 */
export function exportUserTemplateApi() {
  return request({
    url: `${API_PREFIX}/import-export/export-user-template`,
    method: 'get',
    responseType: 'blob',
  }).then(response => {
    exportExcel(response)
  })
}

/**
 * 导出用户
 * @param {object} data
 * @param {object} data.ids                     用户id集合
 * @param {object} data.pageReq                 分页参数
 * @param {number} data.pageReq.id              用户id
 * @param {string} data.pageReq.username        用户名
 * @param {string} data.pageReq.startValidTime  用户过期时间
 * @param {string} data.pageReq.endValidTime    用户过期时间
 */
export function exportUserApi(data) {
  return request({
    url: `${API_PREFIX}/import-export/export-user`,
    method: 'post',
    data,
    responseType: 'blob',
  }).then(response => {
    exportExcel(response)
  })
}

// ~ 角色管理
// =====================================================================================================================
/**
 * 导出角色模板
 */
export function exportRoleTemplateApi() {
  return request({
    url: `${API_PREFIX}/import-export/export-role-template`,
    method: 'get',
    responseType: 'blob',
  }).then(response => {
    exportExcel(response)
  })
}

/**
 * 导出角色
 * @param {object} data                 导出参数
 * @param {array} data.ids              勾选的id集合
 * @param {object} data.pageReq         查询参数
 * @param {array} data.pageReq.ids      角色id
 * @param {string} data.pageReq.name    角色名
 * @param {string} data.pageReq.remark  备注
 */
export function exportRoleApi(data) {
  return request({
    url: `${API_PREFIX}/import-export/export-role`,
    method: 'post',
    data,
    responseType: 'blob',
  }).then(response => {
    exportExcel(response)
  })
}
// ~ 菜单管理
// =====================================================================================================================
/**
 * 导出菜单模板
 */
export function exportMenuTemplateApi() {
  return request({
    url: `${API_PREFIX}/import-export/export-menu-template`,
    method: 'get',
    responseType: 'blob',
  }).then(response => {
    exportExcel(response)
  })
}

/**
 * 导出菜单
 * @param {object} data              导出参数
 * @param {string} data.name         导出参数
 * @param {1|2|3} data.type          菜单类型（1：菜单；2：按钮；3：接口）
 * @param {object} data.permissionId 权限标识
 * @param {object} data.path         路由
 */
export function exportMenuApi(data) {
  return request({
    url: `${API_PREFIX}/import-export/export-menu`,
    method: 'post',
    data,
    responseType: 'blob',
  }).then(response => {
    exportExcel(response)
  })
}

// ~ 字典类型
// =====================================================================================================================
/**
 * 导出字典类型模板
 */
export function exportDictTypeTemplateApi() {
  return request({
    url: `${API_PREFIX}/import-export/export-dict-type-template`,
    method: 'get',
    responseType: 'blob',
  }).then(response => {
    exportExcel(response)
  })
}

/**
 * 导出字典类型
 * @param {object} data                     导出参数
 * @param {array} data.ids                  勾选的id
 * @param {object} data.pageReq             查询参数
 * @param {string} data.pageReq.code        字典类型编码
 * @param {string} data.pageReq.name        字典类型名称
 */
export function exportDictTypeApi(data) {
  return request({
    url: `${API_PREFIX}/import-export/export-dict-type`,
    method: 'post',
    data,
    responseType: 'blob',
  }).then(response => {
    exportExcel(response)
  })
}

// ~ 字典明细
// =====================================================================================================================
/**
 * 导出字典明细模板
 */
export function exportDictTemplateApi() {
  return request({
    url: `${API_PREFIX}/import-export/export-dict-template`,
    method: 'get',
    responseType: 'blob',
  }).then(response => {
    exportExcel(response)
  })
}

/**
 * 导出字典明细
 * @param {object} data                     导出参数
 * @param {array} data.ids                  勾选的id
 * @param {object} data.pageReq             查询参数
 * @param {string} data.pageReq.code        字典类型编码
 * @param {string} data.pageReq.name        字典类型名称
 */
export function exportDictApi(data) {
  return request({
    url: `${API_PREFIX}/import-export/export-dict`,
    method: 'post',
    data,
    responseType: 'blob',
  }).then(response => {
    exportExcel(response)
  })
}

// ~ 字典配置
// =====================================================================================================================
/**
 * 导出字典配置模板
 */
export function exportDictSettingTemplateApi() {
  return request({
    url: `${API_PREFIX}/import-export/export-dict-setting-template`,
    method: 'get',
    responseType: 'blob',
  }).then(response => {
    exportExcel(response)
  })
}

/**
 * 导出字典配置
 * @param {object} data                     导出参数
 * @param {array} data.ids                  勾选的id
 * @param {object} data.pageReq             查询参数
 * @param {string} data.pageReq.code        字典类型编码
 * @param {string} data.pageReq.name        字典类型名称
 */
export function exportDictSettingApi(data) {
  return request({
    url: `${API_PREFIX}/import-export/export-dict-setting`,
    method: 'post',
    data,
    responseType: 'blob',
  }).then(response => {
    exportExcel(response)
  })
}

// ~ 应用管理
// =====================================================================================================================
/**
 * 导出应用模板
 */
export function exportAppTemplateApi() {
  return request({
    url: `${API_PREFIX}/import-export/export-app-template`,
    method: 'get',
    responseType: 'blob',
  }).then(response => {
    exportExcel(response)
  })
}

/**
 * 导出应用
 * @param {object} data                     导出参数
 * @param {array} data.ids                  勾选的id
 * @param {object} data.pageReq             查询参数
 * @param {array} data.pageReq.ids          勾选的id
 * @param {number} data.pageReq.id          应用id
 * @param {string} data.pageReq.name        应用名
 * @param {string} data.pageReq.homePage    应用主页
 * @param {true|false} data.pageReq.enabled 是否激活
 * @param {string} data.pageReq.remark      备注
 */
export function exportAppApi(data) {
  return request({
    url: `${API_PREFIX}/import-export/export-app`,
    method: 'post',
    data,
    responseType: 'blob',
  }).then(response => {
    exportExcel(response)
  })
}
