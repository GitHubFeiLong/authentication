package com.goudong.authentication.server.service.manager;

import com.goudong.authentication.server.rest.req.*;
import com.goudong.authentication.server.rest.resp.BaseDictPageResp;
import com.goudong.authentication.server.rest.resp.BaseDictSettingPageResp;
import com.goudong.authentication.server.rest.resp.BaseDictTypePageResp;
import com.goudong.authentication.server.service.dto.BaseDictDTO;
import com.goudong.authentication.server.service.dto.BaseDictSettingDTO;
import com.goudong.authentication.server.service.dto.BaseDictTypeDTO;
import com.goudong.core.lang.PageResult;

import java.util.List;

/**
 * 类描述：
 * 字典管理服务接口
 * @author chenf
 */
public interface BaseDictManagerService {

    //~BaseDictType
    //==================================================================================================================
    /**
     * 分页查询字典类型
     * @param req   分页参数
     * @return  字典类型分页结果
     */
    PageResult<BaseDictTypePageResp> pageBaseDictType(BaseDictTypePageReq req);

    /**
     * 新增字典类型
     * @param req   新增参数
     * @return      新增字典类型结果
     */
    BaseDictTypeDTO createBaseDictType(BaseDictTypeCreateReq req);

    /**
     * 查询字典类型
     * @param id    字典类型ID
     * @return      字典类型
     */
    BaseDictTypeDTO getBaseDictTypeById(Long id);

    /**
     * 修改字典类型
     * @param req   修改参数
     * @return      修改后字典类型
     */
    BaseDictTypeDTO updateBaseDictType(BaseDictTypeUpdateReq req);

    /**
     * 修改字典类型的激活状态
     * @param req   修改字典类型参数
     * @return      true：修改成功；false：修改失败
     */
    Boolean changeEnabledBaseDictType(BaseDictTypeChangeEnabledReq req);

    /**
     * 批量删除字典类型
     * @param ids   字典类型ID
     * @return  true：删除字典类型成功；false：删除字典类型成功
     */
    Boolean deleteBaseDictTypes(List<Long> ids);

    /**
     * 条件分页查询字典类型下拉
     * @param req   下拉分页参数
     * @return  字典类型下拉分页结果
     */
    PageResult<BaseDictTypeDropDownResp> baseDictTypeDropDown(BaseDictTypeDropDownReq req);

    //~BaseDict
    //==================================================================================================================

    /**
     * 分页查询字典
     * @param req   分页查询字典参数
     * @return      分页字典查询结果
     */
    PageResult<BaseDictPageResp> pageBaseDict(BaseDictPageReq req);

    /**
     * 新增字典
     * @param req   新增字典参数
     * @return      新增后的字典
     */
    BaseDictDTO createBaseDict(BaseDictCreateReq req);

    /**
     * 根据字典主键查询字典
     * @param id    字典主键
     * @return      字典
     */
    BaseDictDTO getBaseDictById(Long id);

    /**
     * 修改字典
     * @param req   修改字典参数
     * @return      修改后的字典
     */
    BaseDictDTO updateBaseDict(BaseDictUpdateReq req);

    /**
     * 修改字典的激活状态
     * @param req   修改字典参数
     * @return      true：修改成功；false：修改失败
     */
    Boolean changeEnabledBaseDict(BaseDictChangeEnabledReq req);

    /**
     * 批量删除字典
     * @param ids  字典主键
     * @return     true：删除字典成功；false：删除字典成功
     */
    Boolean deleteBaseDicts(List<Long> ids);

    /**
     * 条件分页查询字典明细下拉
     * @param req   下拉分页参数
     * @return  字典明细下拉分页结果
     */
    PageResult<BaseDictDropDownResp> baseDictDropDown(BaseDictDropDownReq req);


    //~BaseDictSetting
    //==================================================================================================================
    /**
     * 分页字典配置
     * @param req   分页字典配置参数
     * @return      分页字典配置结果
     */
    PageResult<BaseDictSettingPageResp> pageBaseDictSetting(BaseDictSettingPageReq req);

    /**
     * 新增字典配置
     * @param req   新增字典配置参数
     * @return      新增的字典配置
     */
    BaseDictSettingDTO createBaseDictSetting(BaseDictSettingCreateReq req);

    /**
     * 根据字典配置ID查询字典配置
     * @param id    字典配置ID
     * @return      字典配置
     */
    BaseDictSettingDTO getBaseDictSettingById(Long id);

    /**
     * 修改字典配置
     * @param req   修改参数
     * @return      修改后字典配置
     */
    BaseDictSettingDTO updateBaseDictSetting(BaseDictSettingUpdateReq req);

    /**
     * 修改字典配置的激活状态
     * @param req   修改字典配置参数
     * @return      true：修改成功；false：修改失败
     */
    Boolean changeEnabledBaseDictSetting(BaseDictSettingChangeEnabledReq req);

    /**
     * 批量删除字典配置
     * @param ids   待删除的字典配置主键集合
     * @return  true：删除成功
     */
    Boolean deleteBaseDictSettings(List<Long> ids);

}
