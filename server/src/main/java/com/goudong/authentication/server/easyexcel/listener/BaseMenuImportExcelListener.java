package com.goudong.authentication.server.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.goudong.authentication.common.util.AssertUtil;
import com.goudong.authentication.common.util.CollectionUtil;
import com.goudong.authentication.common.util.ListUtil;
import com.goudong.authentication.common.util.StringUtil;
import com.goudong.authentication.server.config.MyIdentifierGenerator;
import com.goudong.authentication.server.constant.CommonConst;
import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.easyexcel.template.BaseMenuImportExcelTemplate;
import com.goudong.authentication.server.enums.option.MenuTypeEnum;
import com.goudong.authentication.server.exception.ClientException;
import com.goudong.authentication.server.service.BaseMenuService;
import com.goudong.authentication.server.service.BaseRoleService;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.mapper.BaseMenuMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 菜单导入excel监听器
 * @author chenf
 * @version 1.0
 */
@Slf4j
public class BaseMenuImportExcelListener implements ReadListener<BaseMenuImportExcelTemplate> {
    //~fields
    //==================================================================================================================
    /**
     * 缓存的数据
     */
    private final List<BaseMenu> cachedDataList = new ArrayList<>();

    /**
     * 用户信息
     */
    private final MyAuthentication myAuthentication;

    /**
     * 菜单服务
     */
    private final BaseMenuService baseMenuService;

    /**
     * 菜单服务
     */
    private final BaseRoleService baseRoleService;

    /**
     * 实物
     */
    private final TransactionTemplate transactionTemplate;

    /**
     * 导入的菜单类型
     */
    private final Map<String, Integer> types;

    /**
     * BaseMenu实体映射器
     */
    private final BaseMenuMapper baseMenuMapper;

    /**
     * 导入的菜单类型
     */
    private final List<String> methods;


    //~methods
    //==================================================================================================================
    public BaseMenuImportExcelListener(MyAuthentication myAuthentication,
                                       BaseMenuService baseMenuService,
                                       BaseRoleService baseRoleService,
                                       TransactionTemplate transactionTemplate,
                                       BaseMenuMapper baseMenuMapper) {
        this.myAuthentication = myAuthentication;
        this.baseMenuService = baseMenuService;
        this.baseRoleService = baseRoleService;
        this.transactionTemplate = transactionTemplate;
        this.baseMenuMapper = baseMenuMapper;
        this.methods = ListUtil.newArrayList(CommonConst.HTTP_METHODS);
        this.types = new HashMap<>(3);
        this.types.put(MenuTypeEnum.MENU.getLabel(), MenuTypeEnum.MENU.getValue());
        this.types.put(MenuTypeEnum.BUTTON.getLabel(), MenuTypeEnum.BUTTON.getValue());
        this.types.put(MenuTypeEnum.INTERFACE.getLabel(), MenuTypeEnum.INTERFACE.getValue());
    }

    /**
     * When analysis one row trigger invoke function.
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context analysis context
     */
    @Override
    public void invoke(BaseMenuImportExcelTemplate data, AnalysisContext context) {
        log.debug("解析到一条数据:{}", data);
        BaseMenu baseMenu = new BaseMenu();
        // 校验参数
        AssertUtil.isNotBlank(data.getPermissionId(), () -> ClientException.client("权限标识必填"));
        AssertUtil.isNotBlank(data.getName(), () -> ClientException.client("菜单名称必填"));
        AssertUtil.isTrue(types.containsKey(data.getType()), () -> ClientException.client("未知菜单类型：" + data.getType()));
        if (StringUtil.isNotBlank(data.getMethod())) {
            AssertUtil.isTrue(methods.contains(data.getMethod()), () -> ClientException.client("未知请求方式：" + data.getMethod()));
            // 拼接成json
            baseMenu.setMethod("[\"" + data.getMethod() + "\"]");
        }

        if (StringUtil.isNotBlank(data.getMeta())) {
            AssertUtil.isTrue(Pattern.compile("^\\s*\\{.*\\}\\s*$").matcher(data.getMeta()).matches(), () -> ClientException.client("meta 格式错误"));
            // 去掉空格
            baseMenu.setMeta(data.getMeta().replaceAll("\\s+", ""));
        }

        long id = MyIdentifierGenerator.ID.nextId();
        baseMenu.setId(id);
        baseMenu.setAppId(myAuthentication.getRealAppId());
        baseMenu.setPermissionId(data.getPermissionId());
        baseMenu.setParentPermissionId(data.getParentPermissionId());
        baseMenu.setName(data.getName());
        baseMenu.setType(types.get(data.getType()));
        baseMenu.setPath(data.getPath());
        baseMenu.setRemark(data.getRemark());
        baseMenu.setHide(false);

        cachedDataList.add(baseMenu);
    }

    /**
     * if have something to do after all analysis
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 校验所有上级菜单标识在本应用存在
        Map<String, BaseMenu> map2 = new HashMap<>();
        Set<String> parentPermissionIds = new HashSet<>();
        for (BaseMenu baseMenu : cachedDataList) {
            map2.put(baseMenu.getPermissionId(), baseMenu);

            if (StringUtil.isNotBlank(baseMenu.getParentPermissionId())) {
                parentPermissionIds.add(baseMenu.getParentPermissionId());
            }
        }

        // 获取本次导入菜单的父菜单标识不在本次导入的菜单标识中。(如果currentHaveNotParentPermissionIds有值，就需要校验数据库是否有值)
        Collection<String> currentHaveNotParentPermissionIds = CollectionUtil.subtract(parentPermissionIds, map2.keySet());

        // 查询所有菜单
        List<BaseMenuDTO> allMenuDTOList = baseMenuMapper.toDto(baseMenuService.findAllByAppId(myAuthentication.getRealAppId()));
        Map<String, BaseMenuDTO> map1 = new HashMap<>();
        AtomicInteger sortNumAtomicInteger = new AtomicInteger(1);
        for (BaseMenuDTO baseMenuDTO : allMenuDTOList) {
            AssertUtil.isFalse(map2.containsKey(baseMenuDTO.getPermissionId()), () -> ClientException.client("菜单权限标识已存在").serverMessage(baseMenuDTO.getPermissionId()));

            map1.put(baseMenuDTO.getPermissionId(), baseMenuDTO);
            // 找最大的排序号
            if (baseMenuDTO.getSortNum() > sortNumAtomicInteger.get()) {
                sortNumAtomicInteger.set(baseMenuDTO.getSortNum());
            }
        }

        // 将数据库现存菜单的permissionId校验
        if (CollectionUtil.isNotEmpty(currentHaveNotParentPermissionIds)) {
            List<String> notFoundPermissionId = currentHaveNotParentPermissionIds.stream().filter(f -> !map1.containsKey(f)).collect(Collectors.toList());
            AssertUtil.isTrue(CollectionUtil.isEmpty(notFoundPermissionId), () -> ClientException.client().clientMessage("导入菜单失败，存在父菜单标识不在本次导入的菜单标识中，请检查").serverMessage(notFoundPermissionId.toString()));
        }

        // 填充id和
        map2.forEach((k,v)->{
            // 填充排序号
            v.setSortNum(sortNumAtomicInteger.getAndIncrement());
            // 填充parentId
            if (StringUtil.isNotBlank(v.getParentPermissionId())) {
                if (map2.containsKey(v.getParentPermissionId())) {
                    v.setParentId(map2.get(v.getParentPermissionId()).getId());
                }  else if (map1.containsKey(v.getParentPermissionId())) {
                    v.setParentId(map1.get(v.getParentPermissionId()).getId());
                }
            }
        });

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
                    baseMenuService.saveAll(cachedDataList);
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
