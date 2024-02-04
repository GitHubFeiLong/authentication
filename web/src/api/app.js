import request from '@/utils/request'
import {API_PREFIX} from "@/constant/commons";

/**
 * 列表的分页查询
 */
export function pageAppsApi(data) {
  return request({
    url: `${API_PREFIX}/app/page/base-apps`,
    method: 'post',
    data,
  })
}

/**
 * 新增应用
 */
export function createAppApi(data) {
  return request({
    url: `${API_PREFIX}/app/base-app`,
    method: 'post',
    data
  })
}

/**
 * 修改应用
 */
export function updateAppApi(data) {
  return request({
    url: `${API_PREFIX}/app/base-app`,
    method: 'put',
    data
  })
}

/**
 * 删除应用
 * @deprecated  使用{@code deleteAppByIdsApi} 批量删除
 */
export function deleteAppApi(id) {
  return request({
    url: `${API_PREFIX}/app/base-app/${id}`,
    method: 'delete',
  })
}

/**
 * 根据id批量删除应用
 * @param ids 应用id集合
 */
export function deleteAppByIdsApi(ids) {
  return request({
    url: `${API_PREFIX}/app/base-apps`,
    data: ids,
    method: 'delete'
  })
}

/**
 * 新增证书
 */
export function createCertApi(data) {
  return request({
    url: `${API_PREFIX}/app/base-app-cert`,
    method: 'post',
    data
  })
}

/**
 * 查询证书
 */
export function listCertsApi(appId) {
  return request({
    url: `${API_PREFIX}/app/base-app-certs/${appId}`,
    method: 'get'
  })
}
