import request from '@/utils/request'
import {API_PREFIX} from "@/constant/commons";

/**
 * 应用列表的分页查询
 * @param {Object} data 分页参数
 * @param {Number} data.page      分页页码
 * @param {Number} data.size      每页大小
 * @param {Number} data.id        应用id
 * @param {string} data.name      应用名称
 * @param {string} data.homePage  首页地址
 * @param {string} data.enabled   状态
 * @param {string} data.remark    备注
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
 * @param {Object} data             新增应用参数
 * @param {string} data.name        应用名称
 * @param {string} data.homePage    应用中转页
 * @param {'true' | 'false'} data.enabled  激活状态：true 激活；false 未激活
 * @param {string} data.remark      备注信息
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
 * @param {Object} data             修改应用参数
 * @param {Number} data.id          应用id
 * @param {string} data.homePage    应用中转页
 * @param {'0' | '1'} data.enabled  激活状态
 * @param {string} data.remark      备注信息
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
 * @param {Number} id 应用id
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
 * @param {Array} ids 应用id集合
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
 * @param {Object} data             新增证书参数
 * @param {Number} data.appId       应用id
 * @param {string} data.appName     应用名称
 * @param {string} data.validTIme   新增证书参数
 * @param {string} data.remark      新增备注
 */
export function createCertApi(data) {
  return request({
    url: `${API_PREFIX}/app/base-app-cert`,
    method: 'post',
    data
  })
}


/**
 * 新增证书
 * @param {Number} appId  应用id
 */
export function listCertsApi(appId) {
  return request({
    url: `${API_PREFIX}/app/base-app-certs/${appId}`,
    method: 'get'
  })
}
