package com.goudong.authentication.server.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.goudong.authentication.common.util.JsonUtil;
import com.goudong.authentication.server.domain.BaseDict;
import com.goudong.authentication.server.domain.BaseDictSetting;
import com.goudong.authentication.server.easyexcel.template.BaseDictImportExcelTemplate;
import com.goudong.authentication.server.easyexcel.template.BaseDictSettingImportExcelTemplate;
import com.goudong.authentication.server.enums.option.ActivateEnum;
import com.goudong.authentication.server.rest.req.BaseDictTypeDropDownReq;
import com.goudong.authentication.server.rest.req.BaseDictTypeDropDownResp;
import com.goudong.authentication.server.service.BaseDictService;
import com.goudong.authentication.server.service.BaseDictSettingService;
import com.goudong.authentication.server.service.BaseDictTypeService;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.boot.web.core.ClientException;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.StringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 字典配置导入excel监听器
 * @author chenf
 * @version 1.0
 */
@Slf4j
public class BaseDictSettingImportExcelListener implements ReadListener<BaseDictSettingImportExcelTemplate> {
    //~fields
    //==================================================================================================================
    /**
     * 字典详细缓存
     * key：字典明细编码；value：字典明细对应的字典类型ID和字典ID
     */
    private final Map<String, DictDetail> dictDetailMap = new HashMap<>();
    /**
     * 缓存的数据
     */
    private final List<BaseDictSetting> cachedDataList = new ArrayList<>();



    /**
     * 字典服务接口
     */
    private final BaseDictService baseDictService;

    /**
     * 字典类型服务接口
     */
    private final BaseDictSettingService baseDictSettingService;

    /**
     * 事务
     */
    private final TransactionTemplate transactionTemplate;

    /**
     * 当前操作人
     */
    private final MyAuthentication myAuthentication;

    /**
     * 操作时间
     */
    private final Date now = new Date();

    //~methods
    //==================================================================================================================
    public BaseDictSettingImportExcelListener(BaseDictSettingService baseDictSettingService,
                                              BaseDictService baseDictService,
                                              TransactionTemplate transactionTemplate,
                                              MyAuthentication myAuthentication) {
        this.baseDictSettingService = baseDictSettingService;
        this.baseDictService = baseDictService;
        this.transactionTemplate = transactionTemplate;
        this.myAuthentication = myAuthentication;
    }

    /**
     * When analysis one row trigger invoke function.
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context analysis context
     */
    @Override
    public void invoke(BaseDictSettingImportExcelTemplate data, AnalysisContext context) {
        log.debug("解析到一条数据:{}", data);
        // 校验参数
        AssertUtil.isNotBlank(data.getDictCode(), () -> ClientException.client("字典编码必填"));
        AssertUtil.isNotBlank(data.getName(), () -> ClientException.client("配置名称必填"));
        AssertUtil.isNotBlank(data.getEnabled(), () -> ClientException.client("激活状态必填"));

        if (StringUtil.isNotBlank(data.getTemplate())) {
            try {
                JsonUtil.getObjectMapper().readValue(data.getTemplate(), Object.class);
            } catch (JsonProcessingException e) {
                throw ClientException.client("配置模板格式不正确");
            }
        }

        if (StringUtil.isNotBlank(data.getSetting())) {
            try {
                JsonUtil.getObjectMapper().readValue(data.getSetting(), Object.class);
            } catch (JsonProcessingException e) {
                throw ClientException.client("配置格式不正确");
            }
        }
        if (!dictDetailMap.containsKey(data.getDictCode())) {
            log.debug("字典缓存不存在，查询数据是否存在");
            BaseDict baseDict = baseDictService.findByAppIdAndCode(myAuthentication.getRealAppId(), data.getDictCode());
            dictDetailMap.put(data.getDictCode(), new DictDetail(baseDict.getDictTypeId(), baseDict.getId()));
        }
        DictDetail dictDetail = dictDetailMap.get(data.getDictCode());

        BaseDictSetting item = new BaseDictSetting();
        item.setAppId(myAuthentication.getRealAppId());
        item.setDictTypeId(dictDetail.getDictTypeId());
        item.setDictId(dictDetail.getDictId());
        item.setName(data.getName());
        item.setTemplate(data.getTemplate());
        item.setSetting(data.getSetting());
        item.setEnabled(Objects.equals(ActivateEnum.ACTIVATED.getLabel(), data.getEnabled()));
        item.setDefaulted(Objects.equals("默认", data.getDefaulted()));
        item.setRemark(data.getRemark());
        item.setCreatedDate(now);
        item.setLastModifiedDate(now);
        item.setCreatedBy(myAuthentication.getUsername());
        item.setLastModifiedBy(myAuthentication.getUsername());
        cachedDataList.add(item);
    }

    /**
     * if have something to do after all analysis
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        updateData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void updateData() {
        if (CollectionUtils.isNotEmpty(cachedDataList)) {
            transactionTemplate.execute(status -> {
                try {
                    log.info("开始保存保存");
                    cachedDataList.forEach(baseDictSettingService::save);
                    return true;
                }catch (Exception e) {
                    status.setRollbackOnly();
                    // 原封不动抛出，数据库异常才能捕获自定义
                    throw e;
                }
            });
        }
    }

    /**
     * 类描述：
     * 字典详细信息
     * @author chenf
     */
    @Data
    private static class DictDetail {
        private Long dictTypeId;

        private Long dictId;

        public DictDetail(Long dictTypeId, Long dictId) {
            this.dictTypeId = dictTypeId;
            this.dictId = dictId;
        }
    }
}
