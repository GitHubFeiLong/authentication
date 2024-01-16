package com.goudong.authentication.server.easyexcel.listener;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.goudong.authentication.server.constant.CommonConst;
import com.goudong.authentication.server.constant.DateConst;
import com.goudong.authentication.server.domain.BaseRole;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.easyexcel.template.BaseUserImportExcelTemplate;
import com.goudong.authentication.server.enums.option.ActivateEnum;
import com.goudong.authentication.server.enums.option.LockEnum;
import com.goudong.authentication.server.properties.AuthenticationServerProperties;
import com.goudong.authentication.server.service.BaseRoleService;
import com.goudong.authentication.server.service.BaseUserService;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.ListUtil;
import com.goudong.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 类描述：
 * 用户导入excel监听器
 * @author chenf
 */
@Slf4j
public class BaseUserImportExcelListener implements ReadListener<BaseUserImportExcelTemplate> {
    //~fields
    //==================================================================================================================
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<BaseUserImportExcelTemplate> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 用户信息
     */
    private final MyAuthentication myAuthentication;

    /**
     * 用户服务
     */
    private final BaseUserService baseUserService;

    /**
     * 角色服务
     */
    private final BaseRoleService baseRoleService;

    /**
     * 实物
     */
    private final TransactionTemplate transactionTemplate;

    /**
     * 加密器
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * 认证服务配置
     */
    private final AuthenticationServerProperties authenticationServerProperties;

    //~methods
    //==================================================================================================================
    public BaseUserImportExcelListener(AuthenticationServerProperties authenticationServerProperties,
                                       MyAuthentication myAuthentication,
                                       BaseUserService baseUserService,
                                       BaseRoleService baseRoleService,
                                       TransactionTemplate transactionTemplate,
                                       PasswordEncoder passwordEncoder) {
        this.myAuthentication = myAuthentication;
        this.baseUserService = baseUserService;
        this.baseRoleService = baseRoleService;
        this.transactionTemplate = transactionTemplate;
        this.passwordEncoder = passwordEncoder;
        this.authenticationServerProperties = authenticationServerProperties;
    }

    /**
     * When analysis one row trigger invoke function.
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context analysis context
     */
    @Override
    public void invoke(BaseUserImportExcelTemplate data, AnalysisContext context) {
        log.debug("解析到一条数据:{}", data);

        // 检查参数属性
        checkAttribute(data);

        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            updateData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    private void checkAttribute(BaseUserImportExcelTemplate data) {
        // 参数校验
        AssertUtil.isNotBlank(data.getUsername(), "用户名不能为空");

        // 密码判断使用默认密码
        if (StringUtil.isBlank(data.getPassword())) {
            log.debug("未设置密码，使用默认密码");
            data.setPassword(authenticationServerProperties.getApp().getUserDefaultPassword());
        }

        // 角色判断
        AssertUtil.isNotBlank(data.getRoles(), "角色不能为空");
        // 去掉空格
        String roleStr = data.getRoles().replace(" ", "");
        AssertUtil.isFalse(roleStr.startsWith(CommonConst.COMMA), "角色不能以逗号开头");
        // 去掉所有空格，并转换成集合
        String[] splitRoles = roleStr.split(CommonConst.COMMA);
        if (splitRoles.length > 0) {
            List<String> roles = ListUtil.newArrayList(splitRoles);
            // 查询角色
            List<BaseRole> baseRoles = baseRoleService.listByAppIdAndNames(myAuthentication.getRealAppId(), roles);
            // 校验角色要和所查询的一致
            AssertUtil.isTrue(Objects.equals(roles.size(), baseRoles.size()), "请检查角色名称是否有效");
            data.setBaseRoles(baseRoles);
        }

        // 过期时间
        String validTime = data.getValidTime();
        AssertUtil.isNotBlank(validTime, "有效期不能为空");
        // 不包含时间,就追加一个时间
        if (!validTime.contains(":")) {
            data.setValidTime(validTime + " " + DateConst.MAX_TIME_STR);
        }

        AssertUtil.isNotBlank(data.getEnabled(), "激活状态不能为空");
        AssertUtil.isNotBlank(data.getLocked(), "锁定状态不能为空");
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
            log.info("{}条数据，开始存", cachedDataList.size());
            List<BaseUser> users = new ArrayList<>(cachedDataList.size());
            cachedDataList.forEach(p -> {
                BaseUser user = new BaseUser();
                user.setAppId(myAuthentication.getRealAppId());
                user.setRealAppId(myAuthentication.getRealAppId());
                user.setUsername(p.getUsername());
                user.setPassword(passwordEncoder.encode(p.getPassword()));
                user.setRoles(Optional.ofNullable(p.getBaseRoles()).orElseGet(() -> new ArrayList<>(0)));
                user.setEnabled(Objects.equals(ActivateEnum.ACTIVATED.getLabel(), p.getEnabled()));
                user.setLocked(Objects.equals(LockEnum.LOCKED.getLabel(), p.getLocked()));
                user.setValidTime(DateUtil.parse(p.getValidTime()));
                user.setRemark(p.getRemark());
                users.add(user);
            });
            transactionTemplate.execute(status -> {
                try {
                    baseUserService.saveAll(users);
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
