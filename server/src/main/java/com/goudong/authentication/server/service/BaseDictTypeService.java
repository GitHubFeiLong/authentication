package com.goudong.authentication.server.service;

import com.goudong.authentication.server.domain.BaseDictType;
import com.goudong.authentication.server.rest.req.*;
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
    BaseDictType save(BaseDictType req);

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
    BaseDictType update(BaseDictTypeUpdateReq req);

    /**
     * 修改字典类型的激活状态
     *
     * @param req 修改字典类型参数
     * @return true：修改成功；false：修改失败
     */
    Boolean changeEnabled(BaseDictTypeChangeEnabledReq req);

    /**
     * 批量删除字典类型
     *
     * @param ids 被删除的字典类型id集合
     * @return true删除成功；false删除失败
     */
    Boolean deleteByIds(List<Long> ids);

    /**
     * 条件分页查询字典类型下拉
     *
     * @param req 下拉分页参数
     * @return 字典类型下拉分页结果
     */
    PageResult<BaseDictTypeDropDownResp> baseDictTypeDropDown(BaseDictTypeDropDownReq req);
}
