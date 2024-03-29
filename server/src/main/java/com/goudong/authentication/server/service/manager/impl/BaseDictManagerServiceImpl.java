package com.goudong.authentication.server.service.manager.impl;

import com.goudong.authentication.server.rest.req.BaseDictTypeCreateReq;
import com.goudong.authentication.server.rest.req.BaseDictTypePageReq;
import com.goudong.authentication.server.rest.req.BaseDictTypeUpdateReq;
import com.goudong.authentication.server.rest.resp.BaseDictTypePageResp;
import com.goudong.authentication.server.service.BaseDictTypeService;
import com.goudong.authentication.server.service.dto.BaseDictTypeDTO;
import com.goudong.authentication.server.service.manager.BaseDictManagerService;
import com.goudong.authentication.server.service.mapper.BaseDictTypeMapper;
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
     * 字典类型mapper
     */
    @Resource
    private BaseDictTypeMapper baseDictTypeMapper;

    //~methods
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
        return baseDictTypeService.save(req);
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
        return baseDictTypeService.update(req);
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
}
