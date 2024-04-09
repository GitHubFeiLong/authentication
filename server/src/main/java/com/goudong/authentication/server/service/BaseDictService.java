package com.goudong.authentication.server.service;
import com.goudong.authentication.server.domain.BaseDict;
import com.goudong.authentication.server.rest.req.BaseDictChangeEnabledReq;
import com.goudong.authentication.server.rest.req.BaseDictPageReq;
import com.goudong.authentication.server.rest.req.BaseDictUpdateReq;
import com.goudong.authentication.server.rest.resp.BaseDictPageResp;
import com.goudong.authentication.server.service.dto.BaseDictDTO;
import com.goudong.core.lang.PageResult;

import java.util.List;


/**
 * 类描述：
 * Service Interface for managing {@link BaseDict}.
 * @author chenf
 */
public interface BaseDictService {

    /**
     * 分页查询字典
     * @param req   分页查询字典参数
     * @return      分页字典
     */
    PageResult<BaseDictPageResp> page(BaseDictPageReq req);

    /**
     * 新增字典
     * @param baseDict  新增的字典参数
     * @return          新增的字典
     */
    BaseDict save(BaseDict baseDict);

    /**
     * 根据主键查询字典
     * @param id    字典主键
     * @return      字典
     */
    BaseDict findById(Long id);

    /**
     * 修改字典
     * @param req   修改字典参数
     * @return      修改后的字典
     */
    BaseDict update(BaseDictUpdateReq req);

    /**
     * 修改字典的激活状态
     *
     * @param req 修改字典参数
     * @return true：修改成功；false：修改失败
     */
    Boolean changeEnabled(BaseDictChangeEnabledReq req);

    /**
     * 批量删除字典
     * @param ids   字典主键集合
     * @return      true：删除成功；false：删除失败
     */
    Boolean deleteByIds(List<Long> ids);


}
