package com.goudong.authentication.server.service;

import com.goudong.authentication.server.domain.BaseDict;
import com.goudong.authentication.server.domain.BaseDictSetting;
import com.goudong.authentication.server.rest.req.BaseDictSettingChangeEnabledReq;
import com.goudong.authentication.server.rest.req.BaseDictSettingPageReq;
import com.goudong.authentication.server.rest.req.BaseDictSettingUpdateReq;
import com.goudong.authentication.server.rest.resp.BaseDictSettingPageResp;
import com.goudong.core.lang.PageResult;

import java.util.List;

/**
 * 类描述：
 * Service Interface for managing {@link BaseDictSetting}.
 * @author chenf
 */
public interface BaseDictSettingService {

    /**
     * 分页字典配置
     *
     * @param req 分页字典配置参数
     * @return 分页字典配置结果
     */
    PageResult<BaseDictSettingPageResp> page(BaseDictSettingPageReq req);

    /**
     * 根据字典配置主键查询字典配置
     * @param id    字典配置主键
     * @return      字典配置
     */
    BaseDictSetting findById(Long id);

    /**
     * 修改字典配置
     * @param req   修改参数
     * @return      修改后字典配置
     */
    BaseDictSetting update(BaseDictSettingUpdateReq req);

    /**
     * 修改字典配置的激活状态
     *
     * @param req 修改字典配置参数
     * @return true：修改成功；false：修改失败
     */
    Boolean changeEnabled(BaseDictSettingChangeEnabledReq req);

    /**
     * 批量删除字典配置
     * @param ids   字典配置的主键
     * @return  true：删除成功
     */
    Boolean deleteByIds(List<Long> ids);

    /**
     * 新增字典配置
     * @param baseDictSetting   新增的字典配置参数
     * @return                  新增的字典配置
     */
    BaseDictSetting save(BaseDictSetting baseDictSetting);
}
