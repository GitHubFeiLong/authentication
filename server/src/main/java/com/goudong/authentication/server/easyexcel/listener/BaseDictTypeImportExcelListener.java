package com.goudong.authentication.server.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.goudong.authentication.common.util.AssertUtil;
import com.goudong.authentication.common.util.JsonUtil;
import com.goudong.authentication.common.util.StringUtil;
import com.goudong.authentication.server.domain.BaseDictType;
import com.goudong.authentication.server.easyexcel.template.BaseDictTypeImportExcelTemplate;
import com.goudong.authentication.server.enums.option.ActivateEnum;
import com.goudong.authentication.server.exception.ClientException;
import com.goudong.authentication.server.service.BaseDictTypeService;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 类描述：
 * 字典类型导入excel监听器
 * @author chenf
 * @version 1.0
 */
@Slf4j
public class BaseDictTypeImportExcelListener implements ReadListener<BaseDictTypeImportExcelTemplate> {
    //~fields
    //==================================================================================================================
    /**
     * 缓存的数据
     */
    private final List<BaseDictType> cachedDataList = new ArrayList<>();

    /**
     * 字典服务管理接口
     */
    private final BaseDictTypeService baseDictTypeService;

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
    public BaseDictTypeImportExcelListener(BaseDictTypeService baseDictTypeService,
                                           TransactionTemplate transactionTemplate,
                                           MyAuthentication myAuthentication) {
        this.baseDictTypeService = baseDictTypeService;
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
    public void invoke(BaseDictTypeImportExcelTemplate data, AnalysisContext context) {
        log.debug("解析到一条数据:{}", data);
        // 校验参数
        AssertUtil.isNotBlank(data.getCode(), () -> ClientException.client("类型编码必填"));
        AssertUtil.isNotBlank(data.getName(), () -> ClientException.client("类型名称必填"));
        AssertUtil.isNotBlank(data.getEnabled(), () -> ClientException.client("激活状态必填"));

        if (StringUtil.isNotBlank(data.getTemplate())) {
            try {
                JsonUtil.getObjectMapper().readValue(data.getTemplate(), Object.class);
            } catch (JsonProcessingException e) {
                throw ClientException.client("类型配置模板格式不正确");
            }
        }

        BaseDictType item = new BaseDictType();
        item.setAppId(myAuthentication.getRealAppId());
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
                    cachedDataList.forEach(baseDictTypeService::save);
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
