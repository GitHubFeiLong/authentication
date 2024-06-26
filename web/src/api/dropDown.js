/* 下拉api*/
import request from '@/utils/request'
import {API_PREFIX} from "@/constant/commons";

/**
 * 应用下拉
 * @returns {*}
 */
export function dropDownAllAppApi() {
  return request({
    url: `${API_PREFIX}/drop-down/base-app/all-drop-down`,
    method: 'get',
  })
}


/**
 * 用户下拉
 * @param {object} params
 * @param {number} params.id    用户id
 * @param {string} params.name  用户名
 * @returns {*}
 */
export function dropDownUserApi(params) {
  return request({
    url: `${API_PREFIX}/drop-down/base-user/page`,
    method: 'get',
    params: params
  })
}

/**
 * 角色下拉
 * @param {object} params
 * @param {number} params.id    角色id
 * @param {string} params.name  角色名
 * @returns {*}
 */
export function dropDownRoleApi(params) {
  return request({
    url: `${API_PREFIX}/drop-down/base-role/page`,
    method: 'get',
    params: params
  })
}

/**
 * 字典类型下拉
 * @param {object} params
 * @param {String} params.code  字典类型编码
 * @returns {*}
 */
export function dropDownDictTypeApi(params) {
  return request({
    url: `${API_PREFIX}/drop-down/base-dict-type/page`,
    method: 'get',
    params: params
  })
}

/**
 * 字典明细下拉
 * @param {object} params
 * @param {String} params.code  字典明细编码
 * @returns {*}
 */
export function dropDownDictApi(params) {
  return request({
    url: `${API_PREFIX}/drop-down/base-dict/page`,
    method: 'get',
    params: params
  })
}
