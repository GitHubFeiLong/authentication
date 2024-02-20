import request from '@/utils/request'

import {API_PREFIX} from "@/constant/commons";

/**
 * 获取所有菜单
 * @param {object} data 查询参数
 * @param {object} data.name 菜单名称
 * @param {1|2|3} data.type 菜单类型（1：菜单；2：按钮；3：接口）
 * @param {object} data.permissionId 权限标识
 * @param {object} data.path 资源路径
 * @returns {*}
 */
export function listMenuApi(data) {
  return request({
    url: `${API_PREFIX}/menu/base-menus`,
    data,
    method: 'post'
  })
}

/**
 * 初始化菜单
 * @param data
 * @deprecated  不使用
 * @returns {*}
 */
export function initMenuApi(data) {
  return request({
    url: `/api/user/base-menu/init`,
    method: 'post',
    data
  })
}

/**
 * 新增菜单
 * @param {object} data 新增参数
 * @param {undefined|number} data.parentId    上级菜单
 * @param {1|2|3} data.type                   菜单类型（1：菜单；2：按钮；3：接口）
 * @param {string} data.name                  菜单名称
 * @param {string} data.permissionId          权限标识
 * @param {string} data.path                  路由地址
 * @param {string} data.method                请求方式
 * @param {number} data.sortNum               排序号
 * @param {true|false} data.hide              是否展示菜单
 * @param {string} data.meta                  菜单元数据，值是json字符串格式
 * @param {string} data.remark                菜单备注
 * @returns {*}
 */
export function addMenuApi(data) {
  return request({
    url: `${API_PREFIX}/menu/base-menu`,
    method: 'post',
    data
  })
}

/**
 * 修改菜单
 * @param {object} data 修改参数
 * @param {number} data.id                    菜单id
 * @param {undefined|number} data.parentId    上级菜单
 * @param {1|2|3} data.type                   菜单类型（1：菜单；2：按钮；3：接口）
 * @param {string} data.name                  菜单名称
 * @param {string} data.permissionId          权限标识
 * @param {string} data.path                  路由地址
 * @param {string} data.method                请求方式
 * @param {number} data.sortNum               排序号
 * @param {true|false} data.hide              是否展示菜单
 * @param {string} data.meta                  菜单元数据，值是json字符串格式
 * @param {string} data.remark                菜单备注
 * @returns {*}
 */
export function updateMenuApi(data) {
  return request({
    url: `${API_PREFIX}/menu/base-menu`,
    method: 'put',
    data
  })
}

/**
 * 删除菜单
 * @param {number} id 菜单id
 * @deprecated
 */
export function deleteMenuByIdApi(id) {
  return request({
    url: `${API_PREFIX}/menu/base-menu/${id}`,
    method: 'delete',
  })
}

/**
 * 根据id集合批量删除菜单
 * @param {array} ids 菜单id集合
 */
export function deleteMenuByIdsApi(ids) {
  return request({
    url: `${API_PREFIX}/menu/base-menus`,
    data: ids,
    method: 'delete'
  })
}

/**
 * 修改排序
 * @param {object} data
 * @param {number} data.beforeId  移动的菜单id
 * @param {number} data.afterId   移动后所处位置的菜单id
 * @returns {*}
 */
export function changeSortNumApi(data) {
  return request({
    url: `${API_PREFIX}/menu/base-menu/sort-num`,
    method: 'put',
    data: data,
  })
}
