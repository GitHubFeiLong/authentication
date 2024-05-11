import request from '@/utils/request'
import {API_PREFIX} from "@/constant/commons";

// ~ 字典类型
//======================================================================================================================
/**
 * 分页查询字典类型
 * @param {Object}   data       分页参数
 * @param {Number}   data.page  页码
 * @param {Number}   data.size  每页多少条数据
 * @param {String}   data.code  类型编码
 * @param {string}   data.name  类型名称
 */
export function pageDictTypeApi(data) {
  return request({
    url: `${API_PREFIX}/dict/page/base-dict-type`,
    method: 'post',
    data
  })
}

/**
 * 新增字典类型
 * @param {Object} data                     新增字典类型参数
 * @param {string} data.code                字典类型编码
 * @param {string} data.name                字典类型名称
 * @param {string} data.template            字典类型配置模板JSON字符串
 * @param {'true' | 'false'} data.enabled   字典类型激活状态：true 激活；false 未激活
 * @param {string} data.remark              字典类型备注信息
 */
export function createBaseDictTypeApi(data) {
  return request({
    url: `${API_PREFIX}/dict/base-dict-type`,
    method: 'post',
    data
  })
}

/**
 * 根据字典类型ID查询字典类型信息
 * @param {Number | String}   id       字典类型ID
 */
export function getBaseDictTypeByIdApi(id) {
  return request({
    url: `${API_PREFIX}/dict/base-dict-type/${id}`,
    method: 'get',
  })
}

/**
 * 修改字典类型
 * @param {Object} data                     修改字典类型参数
 * @param {Number} data.id                  修改字典类型的ID
 * @param {string} data.code                字典类型编码
 * @param {string} data.name                字典类型名称
 * @param {string} data.template            字典类型配置模板JSON字符串
 * @param {'true' | 'false'} data.enabled   字典类型激活状态：true 激活；false 未激活
 * @param {string} data.remark              字典类型备注信息
 */
export function updateBaseDictTypeApi(data) {
  return request({
    url: `${API_PREFIX}/dict/base-dict-type`,
    method: 'put',
    data
  })
}

/**
 * 修改字典类型激活状态
 * @param {Object} data     修改字典类型参数
 * @param {Number} data.id  修改字典类型的ID
 */
export function changeEnabledBaseDictTypeApi(data) {
  return request({
    url: `${API_PREFIX}/dict/base-dict-type/change-enabled`,
    method: 'put',
    data
  })
}

/**
 * 删除字典类型
 * @param {Array} ids 批量删除字典类型
 */
export function deleteDictTypesApi(ids) {
  return request({
    url: `${API_PREFIX}/dict/base-dict-type`,
    method: 'delete',
    data: ids,
  })
}

// ~ 字典明细
//======================================================================================================================
/**
 * 分页查询字典明细
 * @param {Object}   data             分页参数
 * @param {Number}   data.page        页码
 * @param {Number}   data.size        每页多少条数据
 * @param {Number}   data.dictTypeId  字典类型ID
 * @param {String}   data.code        字典编码
 * @param {string}   data.name        字典名称
 */
export function pageDictApi(data) {
  return request({
    url: `${API_PREFIX}/dict/page/base-dict`,
    method: 'post',
    data
  })
}

/**
 * 根据字典明细ID查询字典明细信息
 * @param {Number | String}   id       字典明细ID
 */
export function getBaseDictByIdApi(id) {
  return request({
    url: `${API_PREFIX}/dict/base-dict/${id}`,
    method: 'get',
  })
}

/**
 * 新增字典明细
 * @param {{template: null, code: null, dictTypeId: null, name: null, remark: null, enabled: boolean}} data                     新增字典明细参数
 * @param {Number| String} data.dictTypeId  字典类型ID
 * @param {string} data.code                字典明细编码
 * @param {string} data.name                字典明细名称
 * @param {string} data.template            字典明细配置模板JSON字符串
 * @param {'true' | 'false'} data.enabled   字典明细激活状态：true 激活；false 未激活
 * @param {string} data.remark              字典明细备注信息
 */
export function createBaseDictApi(data) {
  return request({
    url: `${API_PREFIX}/dict/base-dict`,
    method: 'post',
    data
  })
}

/**
 * 修改字典明细
 * @param {Object} data                     修改字典明细参数
 * @param {Number} data.id                  修改字典明细的ID
 * @param {string} data.code                字典明细编码
 * @param {string} data.name                字典明细名称
 * @param {string} data.template            字典明细配置模板JSON字符串
 * @param {'true' | 'false'} data.enabled   字典明细激活状态：true 激活；false 未激活
 * @param {string} data.remark              字典明细备注信息
 */
export function updateBaseDictApi(data) {
  return request({
    url: `${API_PREFIX}/dict/base-dict`,
    method: 'put',
    data
  })
}

/**
 * 修改字典明细激活状态
 * @param {Object} data     修改字典明细参数
 * @param {Number} data.id  修改字典明细的ID
 */
export function changeEnabledBaseDictApi(data) {
  return request({
    url: `${API_PREFIX}/dict/base-dict/change-enabled`,
    method: 'put',
    data
  })
}

/**
 * 删除字典
 * @param {Array} ids 批量删除字典ID集合
 */
export function deleteDictApi(ids) {
  return request({
    url: `${API_PREFIX}/dict/base-dict`,
    method: 'delete',
    data: ids,
  })
}
// ~ 字典配置
//======================================================================================================================
/**
 * 分页查询字典配置
 * @param {Object}   data             分页参数
 * @param {Number}   data.page        页码
 * @param {Number}   data.size        每页多少条数据
 * @param {Number}   data.dictTypeId  字典类型ID
 * @param {Number}   data.dictId      字典ID
 * @param {string}   data.name        字典配置名称
 */
export function pageDictSettingApi(data) {
  return request({
    url: `${API_PREFIX}/dict/page/base-dict-setting`,
    method: 'post',
    data
  })
}

/**
 * 根据字典配置ID查询字典配置信息
 * @param {Number | String}   id       字典配置ID
 */
export function getBaseDictSettingByIdApi(id) {
  return request({
    url: `${API_PREFIX}/dict/base-dict-setting/${id}`,
    method: 'get',
  })
}

/**
 * 新增字典配置
 * @param {Object} data                     新增字典配置参数
 * @param {Number| String} data.dictId      字典配置ID
 * @param {string} data.name                字典配置名称
 * @param {string} data.template            字典配置模板JSON字符串
 * @param {string} data.setting             字典配置JSON字符串
 * @param {'true' | 'false'} data.enabled   字典激活状态：true 激活；false 未激活
 * @param {'true' | 'false'} data.defaulted 字典是否默认：true 默认；false 否认
 * @param {string} data.remark              字典配置备注信息
 */
export function createBaseDictSettingApi(data) {
  return request({
    url: `${API_PREFIX}/dict/base-dict-setting`,
    method: 'post',
    data
  })
}

/**
 * 修改字典配置
 * @param {Object} data                     修改字典配置参数
 * @param {Number} data.id                  修改字典配置的ID
 * @param {string} data.name                字典配置名称
 * @param {string} data.template            字典配置配置模板JSON字符串
 * @param {string} data.setting             字典配置配置JSON字符串
 * @param {'true' | 'false'} data.enabled   字典激活状态：true 激活；false 未激活
 * @param {'true' | 'false'} data.defaulted 字典是否默认：true 默认；false 否认
 * @param {string} data.remark              字典配置备注信息
 */
export function updateBaseDictSettingApi(data) {
  return request({
    url: `${API_PREFIX}/dict/base-dict-setting`,
    method: 'put',
    data
  })
}

/**
 * 修改字典配置激活状态
 * @param {Object} data     修改字典配置参数
 * @param {Number} data.id  修改字典配置的ID
 */
export function changeEnabledBaseDictSettingApi(data) {
  return request({
    url: `${API_PREFIX}/dict/base-dict-setting/change-enabled`,
    method: 'put',
    data
  })
}

/**
 * 删除字典配置
 * @param {Array} ids 批量删除字典配置ID集合
 */
export function deleteDictSettingApi(ids) {
  return request({
    url: `${API_PREFIX}/dict/base-dict-setting`,
    method: 'delete',
    data: ids,
  })
}
