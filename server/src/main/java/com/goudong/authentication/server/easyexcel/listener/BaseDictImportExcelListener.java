package com.goudong.authentication.server.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.goudong.authentication.common.util.JsonUtil;
import com.goudong.authentication.server.domain.BaseDict;
import com.goudong.authentication.server.domain.BaseDictType;
import com.goudong.authentication.server.easyexcel.template.BaseDictImportExcelTemplate;
import com.goudong.authentication.server.easyexcel.template.BaseDictTypeImportExcelTemplate;
import com.goudong.authentication.server.enums.option.ActivateEnum;
import com.goudong.authentication.server.rest.req.BaseDictTypeDropDownReq;
import com.goudong.authentication.server.rest.req.BaseDictTypeDropDownResp;
import com.goudong.authentication.server.service.BaseDictService;
import com.goudong.authentication.server.service.BaseDictTypeService;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.boot.web.core.ClientException;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 字典明细导入excel监听器
 * @author chenf
 * @version 1.0
 */
@Slf4j
public class BaseDictImportExcelListener implements ReadListener<BaseDictImportExcelTemplate> {
    //~fields
    //==================================================================================================================
    /**
     * 缓存的数据
     */
    private final List<BaseDict> cachedDataList = new ArrayList<>();

    /**
     * 字典类型服务接口
     */
    private final BaseDictTypeService baseDictTypeService;

    /**
     * 字典服务接口
     */
    private final BaseDictService baseDictService;

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
    public BaseDictImportExcelListener(BaseDictTypeService baseDictTypeService,
                                       BaseDictService baseDictService,
                                       TransactionTemplate transactionTemplate,
                                       MyAuthentication myAuthentication) {
        this.baseDictTypeService = baseDictTypeService;
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
    public void invoke(BaseDictImportExcelTemplate data, AnalysisContext context) {
        log.debug("解析到一条数据:{}", data);
        // 校验参数
        AssertUtil.isNotBlank(data.getDictTypeCode(), () -> ClientException.client("类型编码必填"));
        AssertUtil.isNotBlank(data.getCode(), () -> ClientException.client("字典编码必填"));
        AssertUtil.isNotBlank(data.getName(), () -> ClientException.client("字典名称必填"));
        AssertUtil.isNotBlank(data.getEnabled(), () -> ClientException.client("激活状态必填"));

        if (StringUtil.isNotBlank(data.getTemplate())) {
            try {
                JsonUtil.getObjectMapper().readValue(data.getTemplate(), Object.class);
            } catch (JsonProcessingException e) {
                throw ClientException.client("类型配置模板格式不正确");
            }
        }

        BaseDict item = new BaseDict();
        item.setAppId(myAuthentication.getRealAppId());
        item.setDictTypeCode(data.getDictTypeCode());
        item.setCode(data.getCode());
        item.setName(data.getName());
        item.setTemplate(data.getTemplate());
        item.setEnabled(Objects.equals(ActivateEnum.ACTIVATED.getLabel(), data.getEnabled()));
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
                    // 获取字典编码喝字典类型编码，字典编码重复定义时抛出错误信息
                    log.info("开始校验本次导入的字典编码没重复");
                    Set<String> dictCodeSet = new HashSet<>(cachedDataList.size());
                    Set<String> dictTypeCodeSet = new HashSet<>(cachedDataList.size());
                    cachedDataList.forEach(p -> {
                        if (dictCodeSet.contains(p.getCode())) {
                            throw ClientException.client("字典编码不能重复");
                        }
                        dictCodeSet.add(p.getCode());
                        dictTypeCodeSet.add(p.getDictTypeCode());
                    });

                    // 根据字典类型编码，查询出字典类型
                    log.info("查询字典类型编码，根据编码给字典对象设置字典类型id");
                    BaseDictTypeDropDownReq baseDictTypeDropDownReq = new BaseDictTypeDropDownReq();
                    baseDictTypeDropDownReq.setOpenPage(false);
                    PageResult<BaseDictTypeDropDownResp> baseDictTypeDropDownRespPageResult = baseDictTypeService.baseDictTypeDropDown(baseDictTypeDropDownReq);
                    List<BaseDictTypeDropDownResp> dictTypes = baseDictTypeDropDownRespPageResult.getContent();
                    Map<String, Long> codeIdMap = dictTypes.stream().collect(Collectors.toMap(BaseDictTypeDropDownResp::getCode, BaseDictTypeDropDownResp::getId, (k1, k2) -> k1));
                    // 校验字典类型都存在
                    cachedDataList.forEach(p -> {
                        if (codeIdMap.containsKey(p.getDictTypeCode())) {
                            Long dictTypeId = codeIdMap.get(p.getDictTypeCode());
                            p.setDictTypeId(dictTypeId);
                        } else {
                            throw ClientException.client("字典类型编码：" + p.getDictTypeCode() + "不存在，请检查");
                        }
                    });
                    log.info("开始保存保存");
                    cachedDataList.forEach(baseDictService::save);
                    return true;
                }catch (Exception e) {
                    status.setRollbackOnly();
                    // 原封不动抛出，数据库异常才能捕获自定义
                    throw e;
                }
            });
        }
    }

}
