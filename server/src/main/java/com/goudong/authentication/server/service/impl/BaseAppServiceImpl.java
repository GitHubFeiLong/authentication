package com.goudong.authentication.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.zhxu.bs.BeanSearcher;
import com.goudong.authentication.common.util.JsonUtil;
import com.goudong.authentication.server.constant.HttpHeaderConst;
import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.repository.BaseAppRepository;
import com.goudong.authentication.server.repository.BaseMenuRepository;
import com.goudong.authentication.server.repository.BaseRoleRepository;
import com.goudong.authentication.server.repository.BaseUserRepository;
import com.goudong.authentication.server.rest.req.BaseAppUpdate;
import com.goudong.authentication.server.rest.req.search.BaseAppDropDownReq;
import com.goudong.authentication.server.rest.req.search.BaseAppPageReq;
import com.goudong.authentication.server.rest.resp.BaseAppPageResp;
import com.goudong.authentication.server.service.BaseAppService;
import com.goudong.authentication.server.service.dto.BaseAppDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.mapper.BaseAppMapper;
import com.goudong.authentication.server.util.SecurityContextUtil;
import com.goudong.boot.redis.core.RedisTool;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.core.ServerException;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.CollectionUtil;
import com.goudong.core.util.ListUtil;
import com.goudong.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static com.goudong.authentication.server.enums.RedisKeyTemplateProviderEnum.APP_DROP_DOWN;
import static com.goudong.authentication.server.enums.RedisKeyTemplateProviderEnum.APP_ID;

/**
 * Service Implementation for managing {@link BaseApp}.
 */
@Slf4j
@Service
public class BaseAppServiceImpl implements BaseAppService {

    /**
     * 应用表持久层
     */
    @Resource
    private BaseAppRepository baseAppRepository;

    /**
     * 应用映射器
     */
    @Resource
    private BaseAppMapper baseAppMapper;

    /**
     * redis工具
     */
    @Resource
    private RedisTool redisTool;

    /**
     * 实体查询器
     */
    @Resource
    private BeanSearcher beanSearcher;

    /**
     * 角色表持久层
     */
    @Resource
    private BaseRoleRepository baseRoleRepository;

    /**
     * 用户表持久层
     */
    @Resource
    private BaseUserRepository baseUserRepository;

    /**
     * 菜单表持久层
     */
    @Resource
    private BaseMenuRepository baseMenuRepository;

    /**
     * 事务模板
     */
    @Resource
    private TransactionTemplate transactionTemplate;

    /**
     * 请求对象
     */
    @Resource
    private HttpServletRequest httpServletRequest;

    //~methods
    //==================================================================================================================

    /**
     * 根据应用id查询应用
     *
     * @param id 应用id
     * @return 应用对象
     */
    @Override
    public BaseApp getById(Long id) {
        return baseAppRepository.findById(id).orElseThrow(() -> ClientException.client("应用不存在"));
    }

    /**
     * 根据应用id查询应用
     * @param id 应用id
     * @return 应用对象
     */
    @Override
    public BaseApp findById(Long id) {
        String key = APP_ID.getFullKey(id);
        if (Boolean.TRUE.equals(redisTool.hasKey(key))) {
            String appStr = (String)redisTool.get(APP_ID, id);
            return JsonUtil.toObject(appStr, BaseApp.class);
        }
        synchronized (this) {
            if (Boolean.TRUE.equals(redisTool.hasKey(key))) {
                String appStr = (String)redisTool.get(APP_ID, id);
                return JsonUtil.toObject(appStr, BaseApp.class);
            }

            BaseApp baseApp = baseAppRepository.findById(id).orElseThrow(() -> ClientException.client("应用不存在"));
            redisTool.set(APP_ID, JsonUtil.toJsonString(baseApp), id);
            return baseApp;
        }
    }

    /**
     * 应用分页
     * @param req 查询参数
     * @return 分页结果
     */
    @Override
    public PageResult<BaseAppPageResp> page(BaseAppPageReq req) {
        Specification<BaseApp> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> andPredicateList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(req.getIds())) {
                CriteriaBuilder.In<Object> ids = criteriaBuilder.in(root.get("id"));
                req.getIds().forEach(id -> ids.value(id));
                andPredicateList.add(ids);
            }
            if (req.getId() != null) {
                Path<Object> idPath = root.get("id");
                andPredicateList.add(criteriaBuilder.equal(idPath, req.getId()));
            }
            if (StringUtil.isNotBlank(req.getName())) {
                Path<Object> usernamePath = root.get("name");
                andPredicateList.add(criteriaBuilder.like(usernamePath.as(String.class), "%" + req.getName() + "%"));
            }
            if (StringUtil.isNotBlank(req.getHomePage())) {
                Path<Object> homePagePath = root.get("homePage");
                andPredicateList.add(criteriaBuilder.like(homePagePath.as(String.class), "%" + req.getHomePage() + "%"));
            }

            if (req.getEnabled() != null) {
                Path<Object> enabledPath = root.get("enabled");
                andPredicateList.add(criteriaBuilder.equal(enabledPath, req.getEnabled()));
            }

            if (StringUtil.isNotBlank(req.getRemark())) {
                Path<Object> remarkPath = root.get("remark");
                andPredicateList.add(criteriaBuilder.like(remarkPath.as(String.class), "%" + req.getRemark() + "%"));
            }

            return criteriaBuilder.and(andPredicateList.toArray(new Predicate[0]));
        };

        // 单独创建排序对象
        Sort createdDateDesc = Sort.by("createdDate").descending();

        // 开启分页，就需要分页查询
        if (req.getOpenPage()) {
            Pageable pageable = PageRequest.of(req.getPage(), req.getSize(), createdDateDesc);
            Page<BaseApp> appPage = baseAppRepository.findAll(specification, pageable);

            List<BaseAppPageResp> contents = new ArrayList<>(appPage.getContent().size());
            AtomicLong serialNumber = new AtomicLong(req.getStartSerialNumber());
            appPage.getContent().forEach(p -> {
                BaseAppPageResp baseAppPageResp = BeanUtil.copyProperties(p, BaseAppPageResp.class);
                baseAppPageResp.setSerialNumber(serialNumber.getAndIncrement());
                contents.add(baseAppPageResp);
            });

            return new PageResult<BaseAppPageResp>(appPage.getTotalElements(),
                    (long)appPage.getTotalPages(),
                    appPage.getPageable().getPageNumber() + 1L,
                    (long)appPage.getPageable().getPageSize(),
                    contents
            );
        }

        // 没开启分页，就需要查询所有
        List<BaseApp> users = baseAppRepository.findAll(specification, createdDateDesc);

        List<BaseAppPageResp> contents = new ArrayList<>(users.size());
        AtomicLong serialNumber = new AtomicLong(req.getStartSerialNumber());
        users.forEach(p -> {
            BaseAppPageResp baseAppPageResp = BeanUtil.copyProperties(p, BaseAppPageResp.class);
            baseAppPageResp.setSerialNumber(serialNumber.getAndIncrement());
            contents.add(baseAppPageResp);
        });

        return new PageResult<BaseAppPageResp>((long)users.size(),
                1L,
                1L,
                (long)users.size(),
                contents
        );
    }

    /**
     * 新增应用
     *
     * @param app 新增应用参数
     * @return 应用
     */
    @Override
    public BaseAppDTO save(BaseApp app) {
        return baseAppMapper.toDto(baseAppRepository.save(app));
    }

    /**
     * 修改应用
     * @param req 修改应用对象
     * @return 修改应用后的对象
     */
    @Override
    public BaseAppDTO update(BaseAppUpdate req) {
        BaseApp baseApp = baseAppRepository.findById(req.getId()).orElseThrow(() -> ClientException.client("应用不存在"));

        baseApp.setRemark(Optional.ofNullable(req.getRemark()).orElseGet(baseApp::getRemark));
        baseApp.setHomePage(Optional.ofNullable(req.getHomePage()).orElseGet(baseApp::getHomePage));
        baseApp.setEnabled(Optional.ofNullable(req.getEnabled()).orElseGet(baseApp::getEnabled));
        baseAppRepository.save(baseApp);

        cleanCache(req.getId());
        return baseAppMapper.toDto(baseApp);
    }

    /**
     * 删除应用
     * @param id id
     * @return 被删除应用
     */
    @Override
    public BaseAppDTO delete(Long id) {
        AssertUtil.isTrue(baseAppRepository.existsById(id), () -> ClientException.client("应用不存在"));
        transactionTemplate.execute(status -> {
            try {
                // 删除应用
                baseAppRepository.deleteById(id);
                // 删除角色
                int delRoles = baseRoleRepository.deleteByAppId(id);
                log.info("删除{}个角色", delRoles);
                int delUsers = baseUserRepository.deleteByAppId(id);
                log.info("删除{}个用户", delUsers);
                int delMenus = baseMenuRepository.deleteByAppId(id);
                log.info("删除{}个菜单", delMenus);

                return true;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new ServerException("事务异常(删除应用)：" + e.getMessage());
            }
        });
        cleanCache(id);
        log.info("删除应用成功！");
        return null;
    }

    /**
     * 批量删除应用
     *
     * @param ids 应用id集合
     */
    @Override
    public void deleteByIds(Iterable<Long> ids) {
        transactionTemplate.execute(status -> {
            try {
                // 删除应用
                baseAppRepository.deleteAllById(ids);
                // 循环删除用户角色菜单
                ids.forEach(appId -> {
                    baseRoleRepository.deleteByAppId(appId);
                    baseUserRepository.deleteByAppId(appId);
                    baseMenuRepository.deleteByAppId(appId);
                });
                ids.forEach(this::cleanCache);
                return true;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new ServerException("事务异常(删除应用)：" + e.getMessage());
            }
        });
        log.info("删除应用成功！");
    }

    /**
     * 应用下拉
     * @param req 下拉参数
     * @return 用户能访问的应用下拉列表
     */
    @Override
    public List<BaseAppDropDownReq> dropDown(BaseAppDropDownReq req) {
        MyAuthentication authentication = SecurityContextUtil.get();
        // 不是超级管理员只返回自身应用
        if (!authentication.superAdmin()) {
            BaseApp app = findById(authentication.getRealAppId());
            BaseAppDropDownReq baseAppDropDownReq = new BaseAppDropDownReq();
            baseAppDropDownReq.setId(app.getId());
            baseAppDropDownReq.setName(app.getName());
            return ListUtil.newArrayList(baseAppDropDownReq);
        }
        // 超级管理员返回所有应用
        return allDropDown(req);
    }

    /**
     * 应用下拉
     * @param req 下拉参数
     * @return 所有应用下拉
     */
    @Override
    public List<BaseAppDropDownReq> allDropDown(BaseAppDropDownReq req) {
        // 超级管理员返回所有应用
        String key = APP_DROP_DOWN.getFullKey();
        if (Boolean.TRUE.equals(redisTool.hasKey(key))) {
            return redisTool.getList(APP_DROP_DOWN, BaseAppDropDownReq.class);
        }
        synchronized (this) {
            if (Boolean.TRUE.equals(redisTool.hasKey(key))) {
                return redisTool.getList(APP_DROP_DOWN, BaseAppDropDownReq.class);
            }

            List<BaseAppDropDownReq> list = beanSearcher.searchAll(BaseAppDropDownReq.class);

            redisTool.set(APP_DROP_DOWN, list);

            return list;
        }
    }

    /**
     * 根据请求头中{@code X-App-Id}，查询应用
     *
     * @return baseApp
     */
    @Override
    public BaseApp findByHeader() {
        Long appId = (Long)httpServletRequest.getAttribute(HttpHeaderConst.X_APP_ID);
        Assert.notNull(appId, () -> ClientException.client("请求头中应用id不存在"));
        return this.findById(appId);
    }

    /**
     * 判断应用是否存在
     *
     * @param appId 应用id
     * @return true 应用存在；false 应用不存在
     */
    @Override
    public boolean isExist(Long appId) {
        return baseAppRepository.existsById(appId);
    }

    /**
     * 删除缓存
     * @param id 应用id
     */
    public void cleanCache(Long id) {
        redisTool.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                redisTool.deleteKey(APP_ID, id);
                redisTool.deleteKey(APP_DROP_DOWN);
                operations.exec();
                return null;
            }
        });
    }
}
