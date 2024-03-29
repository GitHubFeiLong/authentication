package com.goudong.authentication.server.service;

import com.goudong.authentication.server.domain.BaseDictType;
import com.goudong.authentication.server.rest.req.BaseDictTypeCreateReq;
import com.goudong.authentication.server.rest.req.BaseDictTypePageReq;
import com.goudong.authentication.server.rest.req.BaseDictTypeUpdateReq;
import com.goudong.authentication.server.rest.resp.BaseDictTypePageResp;
import com.goudong.authentication.server.service.dto.BaseDictTypeDTO;
import com.goudong.core.lang.PageResult;

import java.util.List;

/**
 * 类描述：
 * Service Interface for managing {@link BaseDictType}.
 * @author chenf
 */
public interface BaseDictTypeService {

    /**
     * 分页字典类型
     * @param req   分页参数
     * @return      分页结果
     */
    PageResult<BaseDictTypePageResp> page(BaseDictTypePageReq req);

    /**
     * 新增字典类型
     * @param req   新增参数
     * @return      新增的结果
     */
    BaseDictTypeDTO save(BaseDictTypeCreateReq req);

    /**
     * 根据ID查询字典类型
     * @param id    字典类型ID
     * @return      字典类型
     */
    BaseDictType findById(Long id);
    /**
     * 修改字典类型
     * @param req   修改参数
     * @return      修改后结果
     */
    BaseDictTypeDTO update(BaseDictTypeUpdateReq req);

    /**
     * 批量删除字典类型
     *
     * @param ids 被删除的字典类型id集合
     * @return true删除成功；false删除失败
     */
    Boolean deleteByIds(List<Long> ids);
}
