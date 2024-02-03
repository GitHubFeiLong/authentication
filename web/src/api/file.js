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
 * @param data
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
 * @param data
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
 * @param data
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
 * @param data
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
