package com.goudong.authentication.server.service.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.authentication.server.domain.BaseDict;
import com.goudong.authentication.server.domain.BaseDictType;
import com.goudong.authentication.server.rest.req.*;
import com.goudong.authentication.server.rest.resp.BaseDictPageResp;
import com.goudong.authentication.server.rest.resp.BaseDictTypePageResp;
import com.goudong.authentication.server.service.BaseDictService;
import com.goudong.authentication.server.service.BaseDictTypeService;
import com.goudong.authentication.server.service.dto.BaseDictDTO;
import com.goudong.authentication.server.service.dto.BaseDictTypeDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseDictManagerService;
import com.goudong.authentication.server.service.mapper.BaseDictMapper;
import com.goudong.authentication.server.service.mapper.BaseDictTypeMapper;
import com.goudong.authentication.server.util.SecurityContextUtil;
import com.goudong.core.lang.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 类描述：
 * 字典管理服务接口实现类
 * @author chenf
 */
@Service
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
     * 字典类型mapper
     */
    @Resource
    private BaseDictTypeMapper baseDictTypeMapper;

    /**
     * 字典类型mapper
     */
    @Resource
    private BaseDictMapper baseDictMapper;

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
     * 批量删除字典类型
     *
     * @param ids 字典类型ID
     * @return true：删除字典类型成功；false：删除字典类型成功
     */
    @Override
    public Boolean deleteBaseDictTypes(List<Long> ids) {
        return baseDictTypeService.deleteByIds(ids);
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
    public BaseDictDTO createBaseDict(BaseDictCreateReq req) {
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
     * 批量删除字典
     *
     * @param ids 字典主键
     * @return true：删除字典成功；false：删除字典成功
     */
    @Override
    public Boolean deleteBaseDicts(List<Long> ids) {
        return baseDictService.deleteByIds(ids);;
    }
}
