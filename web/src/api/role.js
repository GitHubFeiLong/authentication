import request from '@/utils/request'
import {API_PREFIX} from "@/constant/commons";

/**
 * 分页角色查询
 * @param {object} data         分页参数
 * @param {number} data.page    页码
 * @param {number} data.size    每页多少条数据
 * @param {array} data.ids      角色id集合
 * @param {string} data.name    角色名称
 * @param {string} data.remark  角色备注
 */
export function pageRolesApi(data) {
  return request({
    url: `${API_PREFIX}/role/page/base-roles`,
    method: 'post',
    data
  })
}

/**
 * 新增角色
 * @param {object} data           新增角色参数
 * @param {string} data.name      角色名称
 * @param {string} data.remark    角色备注
 * @returns {*}
 */
export function createRoleApi(data) {
  return request({
    url: `${API_PREFIX}/role/base-role`,
    method: 'post',
    data
  })
}

/**
 * 修改角色
 * @param {object} data           修改角色参数
 * @param {string} data.name      角色名称
 * @param {string} data.remark    角色备注
 * @returns {*}
 */
export function updateRoleApi(data) {
  return request({
    url: `${API_PREFIX}/role/base-role`,
    method: 'put',
    data
  })
}

/**
 * 根据id批量删除用户
 * @param {array} ids 用户id集合
 * @returns {*}
 */
export function deleteRoleByIdsApi(ids) {
  return request({
    url: `${API_PREFIX}/role/base-roles`,
    data: ids,
    method: 'delete'
  })
}

/**
 * 查询角色以及角色的权限
 * @param {number} id 角色id
 * @returns {*}
 */
export function getPermissionListByIdApi(id) {
  return request({
    url: `${API_PREFIX}/role/base-role/permission-list/${id}`,
    method: 'get',
  })
}

/**
 * 修改角色权限
 * @param {object} data         修改角色权限参数
 * @param {object} data.id      角色id
 * @param {array} data.menuIds  菜单id集合
 * @returns {*}
 */
export function changePermissionApi(data) {
  return request({
    url: `${API_PREFIX}/role/base-role/permission-list`,
    method: 'post',
    data: data,
  })
}
