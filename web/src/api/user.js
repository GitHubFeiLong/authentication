import request from '@/utils/request'
import {API_PREFIX} from "@/constant/commons";

/**
 * 登录
 * @param {string} username  用户名
 * @param {string} password  密码
 * @param {Number} selectAppId 选择的应用Id
 */
export function loginApi(username, password, selectAppId) {
  return request({
    url: `${API_PREFIX}/user/login?username=${username}&password=${password}&appId=${selectAppId}`,
    method: 'post'
  })
}

/**
 * 刷新token
 * @param {string} refreshToken 刷新令牌
 * @returns {*}
 */
export function refreshTokenApi(refreshToken) {
  return request({
    url: `${API_PREFIX}/user/refresh-token`,
    method: 'post',
    data: {refreshToken: refreshToken}
  })
}

/**
 * 根据token获取用户信息 - get请求
 * @param {string} token  令牌
 * @returns {*}
 */
export function getUserDetailApi(token) {
  return request({
    url: `${API_PREFIX}/user/base-user/token/detail/${token}`,
    method: 'get'
  })
}

/**
 * 根据token获取用户信息 - post请求
 * @param {string} token  令牌
 * @returns {*}
 */
export function getUserDetailApiByPost(token) {
  return request({
    url: `${API_PREFIX}/user/base-user/token/detail`,
    method: 'post',
    data: {token: token},
  })
}

/**
 * 用户的分页查询
 * @param {Object}   data                 分页参数
 * @param {Number}   data.page            页码
 * @param {Number}   data.size            每页多少条数据
 * @param {Number}   data.id              用户id
 * @param {string}   data.username        用户名
 * @param {string}   data.startValidTime  有效日期开始时间
 * @param {string}   data.endValidTime    有效日期截止时间
 */
export function pageUsersApi(data) {
  return request({
    url: `${API_PREFIX}/user/page/base-users`,
    method: 'post',
    data: data,
  })
}

/**
 * 创建用户
 * @param {Object} user 新建用户参数
 * @param {string} user.username  用户名
 * @param {string} user.password  密码
 * @param {array} user.roleIds    角色集合
 * @param {string} user.remark    用户备注
 * @returns {*}
 */
export function simpleCreateUserApi(user) {
  return request({
    url: `${API_PREFIX}/user/base-user/simple-create`,
    method: 'post',
    data: user
  })
}

/**
 * 修改用户
 * @param {Object} user                     修改用户参数
 * @param {Number} user.id                  用户id
 * @param {string} user.password            密码
 * @param {array} user.roleIds              角色集合
 * @param {string} user.validTime           用户有效期
 * @param {'true' | 'false'} user.enabled   激活状态：true 激活；false 未激活
 * @param {'true' | 'false'} user.locked    锁定状态：true 锁定；false 未锁定
 * @param {string} user.remark              用户备注
 * @returns {*}
 */
export function simpleUpdateUserApi(user) {
  return request({
    url: `${API_PREFIX}/user/base-user/simple-update`,
    method: 'put',
    data: user
  })
}

/**
 * 根据id重置用户密码
 * @param {Number} userId 用户id
 * @returns {*}
 */
export function resetPasswordApi(userId) {
  return request({
    url: `${API_PREFIX}/user/base-user/reset-password/${userId}`,
    method: 'put'
  })
}

/**
 * 根据id切换用户激活状态
 * @param {Number} userId 用户id
 * @returns {*}
 */
export function changeEnabledApi(userId) {
  return request({
    url: `${API_PREFIX}/user/base-user/change-locked/${userId}`,
    method: 'put'
  })
}

/**
 * 根据id切换用户锁定状态
 * @param {Number} userId 用户id
 * @returns {*}
 */
export function changeLockedApi(userId) {
  return request({
    url: `${API_PREFIX}/user/base-user/change-locked/${userId}`,
    method: 'put'
  })
}

/**
 * 根据id批量删除用户
 * @param {array} ids 用户id集合
 * @returns {*}
 */
export function deleteUserByIdsApi(ids) {
  return request({
    url: `${API_PREFIX}/user/base-users`,
    data: ids,
    method: 'delete'
  })
}

/**
 * 修改账户密码
 * @param {Object} data  密码
 * @param {string} data.oldPassword 旧密码
 * @param {string} data.newPassword 新密码
 * @returns {*}
 */
export function changeOwnPasswordApi(data) {
  return request({
    url: `${API_PREFIX}/user/base-user/change-password`,
    data,
    method: 'put'
  })
}
