package com.goudong.authentication.server.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.goudong.authentication.server.config.MyIdentifierGenerator;
import com.goudong.authentication.server.constant.CommonConst;
import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.easyexcel.template.BaseAppImportExcelTemplate;
import com.goudong.authentication.server.easyexcel.template.BaseMenuImportExcelTemplate;
import com.goudong.authentication.server.enums.option.ActivateEnum;
import com.goudong.authentication.server.enums.option.MenuTypeEnum;
import com.goudong.authentication.server.rest.req.BaseAppCreate;
import com.goudong.authentication.server.service.BaseMenuService;
import com.goudong.authentication.server.service.BaseRoleService;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseAppManagerService;
import com.goudong.authentication.server.service.mapper.BaseMenuMapper;
import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.core.ClientException;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.CollectionUtil;
import com.goudong.core.util.ListUtil;
import com.goudong.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 应用导入excel监听器
 * @author chenf
 * @version 1.0
 */
@Slf4j
public class BaseAppImportExcelListener implements ReadListener<BaseAppImportExcelTemplate> {
    //~fields
    //==================================================================================================================
    /**
     * 缓存的数据
     */
    private final List<BaseAppCreate> cachedDataList = new ArrayList<>();

    /**
     * 菜单服务
     */
    private final BaseAppManagerService baseAppManagerService;

    /**
     * 实物
     */
    private final TransactionTemplate transactionTemplate;

    /**
     * httpUrl的正则
     */
    private final Pattern httpUrlPattern = Pattern.compile("(http|https)://.*");


    //~methods
    //==================================================================================================================
    public BaseAppImportExcelListener(BaseAppManagerService baseAppManagerService,
                                      TransactionTemplate transactionTemplate) {
        this.baseAppManagerService = baseAppManagerService;
        this.transactionTemplate = transactionTemplate;
    }

    /**
     * When analysis one row trigger invoke function.
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context analysis context
     */
    @Override
    public void invoke(BaseAppImportExcelTemplate data, AnalysisContext context) {
        log.debug("解析到一条数据:{}", data);
        // 校验参数
        AssertUtil.isNotBlank(data.getName(), () -> ClientException.client("应用名称必填"));
        AssertUtil.isNotBlank(data.getEnabled(), () -> ClientException.client("应用状态必填"));

        BaseAppCreate baseAppCreate = new BaseAppCreate();
        baseAppCreate.setEnabled(Objects.equals(ActivateEnum.ACTIVATED.getLabel(), data.getEnabled()));
        baseAppCreate.setName(data.getName());
        baseAppCreate.setHomePage(data.getHomePage());
        baseAppCreate.setRemark(data.getRemark());
        if (Boolean.TRUE.equals(baseAppCreate.getEnabled())) {
            String homePage = data.getHomePage();
            boolean matches = httpUrlPattern.matcher(homePage).matches();
            AssertUtil.isTrue(StringUtil.isNotBlank(homePage) && matches, () ->
                    BasicException.builder()
                            .clientMessageTemplate("应用\"{}\"首页格式错误")
                            .clientMessageParams(data.getName())
                            .serverMessage("已激活的应用，首页地址必填")
                            .build()
            );
        }

        cachedDataList.add(baseAppCreate);
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
                    cachedDataList.forEach(baseAppManagerService::save);
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
