package com.goudong.authentication.server.service.manager;

import com.goudong.authentication.server.rest.req.BaseDictTypeCreateReq;
import com.goudong.authentication.server.rest.req.BaseDictTypePageReq;
import com.goudong.authentication.server.rest.req.BaseDictTypeUpdateReq;
import com.goudong.authentication.server.rest.resp.BaseDictTypePageResp;
import com.goudong.authentication.server.service.dto.BaseDictTypeDTO;
import com.goudong.core.lang.PageResult;

import java.util.List;

/**
 * 类描述：
 * 字典管理服务接口
 * @author chenf
 */
public interface BaseDictManagerService {

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
}
