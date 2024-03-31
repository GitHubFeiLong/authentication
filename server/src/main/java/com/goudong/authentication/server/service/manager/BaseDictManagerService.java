package com.goudong.authentication.server.service.manager;

import com.goudong.authentication.server.rest.req.*;
import com.goudong.authentication.server.rest.resp.BaseDictPageResp;
import com.goudong.authentication.server.rest.resp.BaseDictTypePageResp;
import com.goudong.authentication.server.service.dto.BaseDictDTO;
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
     * 批量删除字典类型
     * @param ids   字典类型ID
     * @return  true：删除字典类型成功；false：删除字典类型成功
     */
    Boolean deleteBaseDictTypes(List<Long> ids);

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
     * 批量删除字典
     * @param ids  字典主键
     * @return     true：删除字典成功；false：删除字典成功
     */
    Boolean deleteBaseDicts(List<Long> ids);
}
