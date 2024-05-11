package com.goudong.authentication.server.service.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.zhxu.bs.SearchResult;
import com.goudong.authentication.server.domain.BaseDict;
import com.goudong.authentication.server.domain.BaseDictSetting;
import com.goudong.authentication.server.domain.BaseDictType;
import com.goudong.authentication.server.rest.req.*;
import com.goudong.authentication.server.rest.req.search.BaseRoleDropDownReq;
import com.goudong.authentication.server.rest.resp.BaseDictPageResp;
import com.goudong.authentication.server.rest.resp.BaseDictSettingPageResp;
import com.goudong.authentication.server.rest.resp.BaseDictTypePageResp;
import com.goudong.authentication.server.rest.resp.BaseRoleDropDownResp;
import com.goudong.authentication.server.service.BaseDictService;
import com.goudong.authentication.server.service.BaseDictSettingService;
import com.goudong.authentication.server.service.BaseDictTypeService;
import com.goudong.authentication.server.service.dto.BaseDictDTO;
import com.goudong.authentication.server.service.dto.BaseDictSettingDTO;
import com.goudong.authentication.server.service.dto.BaseDictTypeDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseDictManagerService;
import com.goudong.authentication.server.service.mapper.BaseDictMapper;
import com.goudong.authentication.server.service.mapper.BaseDictSettingMapper;
import com.goudong.authentication.server.service.mapper.BaseDictTypeMapper;
import com.goudong.authentication.server.util.BeanSearcherUtil;
import com.goudong.authentication.server.util.PageResultUtil;
import com.goudong.authentication.server.util.SecurityContextUtil;
import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.core.ClientException;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 类描述：
 * 字典管理服务接口实现类
 * @author chenf
 */
@Service
@Slf4j
public class BaseDictManagerServiceImpl implements BaseDictManagerService {

    //~fields
    //==================================================================================================================
    /**
     * 字典类型服务层接口
     */
    @Resource
    private BaseDictTypeService baseDictTypeService;

    /**
     * 字典服务层接口
     */
    @Resource
    private BaseDictService baseDictService;

    /**
     * 字典服务层接口
     */
    @Resource
    private BaseDictSettingService baseDictSettingService;

    /**
     * 字典类型mapper
     */
    @Resource
    private BaseDictTypeMapper baseDictTypeMapper;

    /**
     * 字典mapper
     */
    @Resource
    private BaseDictMapper baseDictMapper;

    /**
     * 字典配置mapper
     */
    @Resource
    private BaseDictSettingMapper baseDictSettingMapper;

    //~methods
    //==================================================================================================================
    //~BaseDictType
    //==================================================================================================================
    /**
     * 分页查询字典类型
     *
     * @param req 分页参数
     * @return 字典类型分页结果
     */
    @Override
    public PageResult<BaseDictTypePageResp> pageBaseDictType(BaseDictTypePageReq req) {
        return baseDictTypeService.page(req);
    }

    /**
     * 新增字典类型
     *
     * @param req 新增参数
     * @return 新增字典结果
     */
    @Override
    public BaseDictTypeDTO createBaseDictType(BaseDictTypeCreateReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        BaseDictType baseDictType = BeanUtil.copyProperties(req, BaseDictType.class);
        baseDictType.setAppId(myAuthentication.getRealAppId());
        return baseDictTypeMapper.toDto(baseDictTypeService.save(baseDictType));
    }

    /**
     * 查询字典类型
     *
     * @param id    字典类型ID
     * @return      字典类型
     */
    @Override
    public BaseDictTypeDTO getBaseDictTypeById(Long id) {
        return baseDictTypeMapper.toDto(baseDictTypeService.findById(id));
    }

    /**
     * 修改字典类型
     *
     * @param req 修改参数
     * @return 修改后字典类型
     */
    @Override
    public BaseDictTypeDTO updateBaseDictType(BaseDictTypeUpdateReq req) {
        return baseDictTypeMapper.toDto(baseDictTypeService.update(req));
    }

    /**
     * 修改字典类型的激活状态
     *
     * @param req 修改字典类型参数
     * @return true：修改成功；false：修改失败
     */
    @Override
    public Boolean changeEnabledBaseDictType(BaseDictTypeChangeEnabledReq req) {
        return baseDictTypeService.changeEnabled(req);
    }

    /**
     * 批量删除字典类型
     *
     * @param ids 字典类型ID
     * @return true：删除字典类型成功；false：删除字典类型成功
     */
    @Override
    @Transactional
    public Boolean deleteBaseDictTypes(List<Long> ids) {
        log.info("删除字典类型：{}", ids);
        baseDictTypeService.deleteByIds(ids);
        log.info("删除字典明细");
        baseDictService.deleteByDictTypeIds(ids);
        log.info("删除字典配置");
        baseDictSettingService.deleteByDictTypeIds(ids);
        return true;
    }

    /**
     * 条件分页查询字典类型下拉
     *
     * @param req 下拉分页参数
     * @return 字典类型下拉分页结果
     */
    @Override
    public PageResult<BaseDictTypeDropDownResp> baseDictTypeDropDown(BaseDictTypeDropDownReq req) {
        return baseDictTypeService.baseDictTypeDropDown(req);
    }

    //~BaseDict
    //==================================================================================================================
    /**
     * 分页查询字典
     *
     * @param req 分页查询字典参数
     * @return 分页字典查询结果
     */
    @Override
    public PageResult<BaseDictPageResp> pageBaseDict(BaseDictPageReq req) {
        return baseDictService.page(req);
    }

    /**
     * 新增字典
     *
     * @param req 新增字典参数
     * @return 新增后的字典
     */
    @Override
    @Transactional
    public BaseDictDTO createBaseDict(BaseDictCreateReq req) {
        // 参数校验
        baseDictTypeService.findById(req.getDictTypeId());

        MyAuthentication myAuthentication = SecurityContextUtil.get();
        BaseDict baseDict = BeanUtil.copyProperties(req, BaseDict.class);
        baseDict.setAppId(myAuthentication.getRealAppId());

        return baseDictMapper.toDto(baseDictService.save(baseDict));
    }

    /**
     * 根据字典主键查询字典
     *
     * @param id 字典主键
     * @return 字典
     */
    @Override
    public BaseDictDTO getBaseDictById(Long id) {
        return baseDictMapper.toDto(baseDictService.findById(id));
    }

    /**
     * 修改字典
     *
     * @param req 修改字典参数
     * @return 修改后的字典
     */
    @Override
    public BaseDictDTO updateBaseDict(BaseDictUpdateReq req) {
        return baseDictMapper.toDto(baseDictService.update(req));
    }

    /**
     * 修改字典的激活状态
     *
     * @param req 修改字典参数
     * @return true：修改成功；false：修改失败
     */
    @Override
    public Boolean changeEnabledBaseDict(BaseDictChangeEnabledReq req) {
        return baseDictService.changeEnabled(req);
    }

    /**
     * 批量删除字典
     *
     * @param ids 字典主键
     * @return true：删除字典成功；false：删除字典成功
     */
    @Override
    public Boolean deleteBaseDicts(List<Long> ids) {
        log.info("删除字典明细");
        baseDictService.deleteByIds(ids);
        log.info("删除字典配置");
        baseDictSettingService.deleteByDictIds(ids);
        return true;
    }

    /**
     * 条件分页查询字典明细下拉
     *
     * @param req   下拉分页参数
     * @return  字典明细下拉分页结果
     */
    @Override
    public PageResult<BaseDictDropDownResp> baseDictDropDown(BaseDictDropDownReq req) {
        return baseDictService.baseDictDropDown(req);
    }

    //~BaseDictSetting
    //==================================================================================================================
    /**
     * 分页字典配置
     *
     * @param req 分页字典配置参数
     * @return 分页字典配置结果
     */
    @Override
    public PageResult<BaseDictSettingPageResp> pageBaseDictSetting(BaseDictSettingPageReq req) {
        return baseDictSettingService.page(req);
    }

    /**
     * 新增字典配置
     *
     * @param req 新增字典配置参数
     * @return 新增的字典配置
     */
    @Override
    public BaseDictSettingDTO createBaseDictSetting(BaseDictSettingCreateReq req) {
        BaseDict dict = baseDictService.findById(req.getDictId());
        req.attributeDefaultValue();
        BaseDictSetting baseDictSetting = BeanUtil.copyProperties(req, BaseDictSetting.class);
        baseDictSetting.setDictTypeId(dict.getDictTypeId());
        // 保存
        BaseDictSetting save = baseDictSettingService.save(baseDictSetting);
        return baseDictSettingMapper.toDto(save);
    }

    /**
     * 根据字典配置ID查询字典配置
     *
     * @param id 字典配置ID
     * @return 字典配置
     */
    @Override
    public BaseDictSettingDTO getBaseDictSettingById(Long id) {
        return baseDictSettingMapper.toDto(baseDictSettingService.findById(id));
    }

    /**
     * 修改字典配置
     *
     * @param req 修改参数
     * @return 修改后字典配置
     */
    @Override
    public BaseDictSettingDTO updateBaseDictSetting(BaseDictSettingUpdateReq req) {
        return baseDictSettingMapper.toDto(baseDictSettingService.update(req));
    }

    /**
     * 修改字典配置的激活状态
     *
     * @param req 修改字典配置参数
     * @return true：修改成功；false：修改失败
     */
    @Override
    public Boolean changeEnabledBaseDictSetting(BaseDictSettingChangeEnabledReq req) {
        return baseDictSettingService.changeEnabled(req);
    }

    /**
     * 修改字典配置的默认状态
     *
     * @param req 修改字典配置参数
     * @return true：修改成功；false：修改失败
     */
    @Override
    public Boolean changeDefaultedBaseDictSetting(BaseDictSettingChangeDefaultedReq req) {
        return baseDictSettingService.changeDefaulted(req);
    }

    /**
     * 批量删除字典配置
     *
     * @param ids 待删除的字典配置主键集合
     * @return true：删除成功
     */
    @Override
    public Boolean deleteBaseDictSettings(List<Long> ids) {
        return baseDictSettingService.deleteByIds(ids);
    }
}
