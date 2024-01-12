package com.goudong.authentication.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.zhxu.bs.BeanSearcher;
import cn.zhxu.bs.SearchResult;
import com.goudong.authentication.server.domain.BaseRole;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.repository.BaseUserRepository;
import com.goudong.authentication.server.rest.req.BaseUserPageReq;
import com.goudong.authentication.server.rest.req.search.BaseUserDropDownReq;
import com.goudong.authentication.server.rest.resp.BaseRoleDropDownResp;
import com.goudong.authentication.server.rest.resp.BaseUserDropDownResp;
import com.goudong.authentication.server.rest.resp.BaseUserPageResp;
import com.goudong.authentication.server.service.BaseUserService;
import com.goudong.authentication.server.service.dto.BaseUserDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.mapper.BaseUserMapper;
import com.goudong.authentication.server.util.BeanSearcherUtil;
import com.goudong.authentication.server.util.PageResultUtil;
import com.goudong.authentication.server.util.SecurityContextUtil;
import com.goudong.boot.redis.core.RedisTool;
import com.goudong.boot.web.core.ClientException;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link BaseUser}.
 */
@Slf4j
@Service
public class BaseUserServiceImpl implements BaseUserService {

    //~fields
    //==================================================================================================================
    @Resource
    private BaseUserRepository baseUserRepository;

    @Resource
    private BaseUserMapper baseUserMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private BeanSearcher beanSearcher;

    @Resource
    private RedisTool redisTool;

    //~methods
    //==================================================================================================================
    /**
     * 根据应用Id和用户名查询用户
     *
     * @param appId    应用Id
     * @param username 用户名
     * @return 返回用户
     */
    @Override
    @Transactional
    public BaseUser findOneByAppIdAndUsername(Long appId, String username) {
        BaseUser baseUser = baseUserRepository.findByLogin(appId, username);
        // 懒加载,必须使用才能加载
        if (baseUser !=null) {
            List<String> roleNames = baseUser.getRoles().stream().map(BaseRole::getName).collect(Collectors.toList());
        }
        return baseUser;
    }

    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return 用户对象
     */
    @Override
    @Transactional(readOnly = true)
    public BaseUser findById(Long id) {
        // 查询角色，权限使用
        BaseUser baseUser = baseUserRepository.findById(id).orElseThrow(() -> ClientException.client("用户不存在"));
        baseUser.getRoles().stream().map(BaseRole::getMenus).collect(Collectors.toList());
        return baseUser;
    }

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return 用户对象详细信息（保留角色菜单）
     */
    @Override
    @Transactional(readOnly = true)
    public BaseUser findDetailById(Long id) {
        // 查询角色，权限使用
        BaseUser baseUser = baseUserRepository.findById(id).orElseThrow(() -> ClientException.client("用户不存在"));
        // 简单获取角色和菜单，即可进行查询
        List<String> roleNames = new ArrayList<>();
        List<String> menuNames = new ArrayList<>();
        baseUser.getRoles().forEach(role -> {
            roleNames.add(role.getName());
            role.getMenus().forEach(menu -> {
                menuNames.add(menu.getName());
            });
        });
        return baseUser;
    }

    /**
     * 分页获取用户下拉，只返回操作人所在真实应用下的用户
     *
     * @param req 请求参数
     * @return 用户下拉列表
     */
    @Override
    public PageResult<BaseUserDropDownResp> userDropDown(BaseUserDropDownReq req) {
        MyAuthentication authentication = SecurityContextUtil.get();
        req.setRealAppId(authentication.getRealAppId());
        SearchResult<BaseUserDropDownReq> search = beanSearcher.search(BaseUserDropDownReq.class, BeanSearcherUtil.getParaMap(req));
        return PageResultUtil.convert(search, req, BaseUserDropDownResp.class);
    }

    /**
     * 分页查询用户
     *
     * @param req 分页参数
     * @return 用户分页对象
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<BaseUserPageResp> page(BaseUserPageReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();

        Specification<BaseUser> specification = new Specification<BaseUser>() {
            @Override
            public Predicate toPredicate(Root<BaseUser> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> andPredicateList = new ArrayList<>();
                andPredicateList.add(criteriaBuilder.equal(root.get("realAppId"), myAuthentication.getRealAppId()));
                //1.获取比较的属性
                if (req.getId() != null) {
                    Path<Object> idPath = root.get("id");
                    andPredicateList.add(criteriaBuilder.equal(idPath, req.getId()));
                }
                if (StringUtil.isNotBlank(req.getUsername())) {
                    Path<Object> usernamePath = root.get("username");
                    andPredicateList.add(criteriaBuilder.like(usernamePath.as(String.class), "%" + req.getUsername() + "%"));
                }
                if (req.getStartValidTime() != null && req.getEndValidTime() != null) {
                    andPredicateList.add(criteriaBuilder.between(root.get("validTime"), req.getStartValidTime(), req.getEndValidTime()));
                }

                return criteriaBuilder.and(andPredicateList.toArray(new Predicate[andPredicateList.size()]));
            }
        };

        // 单独创建排序对象
        Sort createdDateDesc = Sort.by("createdDate").descending();

        // 开启分页，就需要分页查询
        if (req.getOpenPage()) {
            Pageable pageable = PageRequest.of(req.getPage(), req.getSize(), createdDateDesc);
            Page<BaseUser> userPage = baseUserRepository.findAll(specification, pageable);

            List<BaseUserPageResp> contents = new ArrayList<>(userPage.getContent().size());
            AtomicLong serialNumber = new AtomicLong(req.getStartSerialNumber());
            userPage.getContent().forEach(p -> {
                List<BaseRoleDropDownResp> roleDropDownRespList = new ArrayList<>(p.getRoles().size());
                p.getRoles().forEach(role -> {
                    roleDropDownRespList.add(new BaseRoleDropDownResp(role.getId(), role.getName()));
                });
                BaseUserPageResp baseUserPageResp = BeanUtil.copyProperties(p, BaseUserPageResp.class);
                baseUserPageResp.setSerialNumber(serialNumber.getAndIncrement());
                contents.add(baseUserPageResp);
            });

            return new PageResult<BaseUserPageResp>(userPage.getTotalElements(),
                    (long)userPage.getTotalPages(),
                    userPage.getPageable().getPageNumber() + 1L,
                    (long)userPage.getPageable().getPageSize(),
                    contents
            );
        }

        // 没开启分页，就需要查询所有
        List<BaseUser> users = baseUserRepository.findAll(specification, createdDateDesc);

        List<BaseUserPageResp> contents = new ArrayList<>(users.size());
        AtomicLong serialNumber = new AtomicLong(req.getStartSerialNumber());
        users.forEach(p -> {
            List<BaseRoleDropDownResp> roleDropDownRespList = new ArrayList<>(p.getRoles().size());
            p.getRoles().forEach(role -> {
                roleDropDownRespList.add(new BaseRoleDropDownResp(role.getId(), role.getName()));
            });
            BaseUserPageResp baseUserPageResp = BeanUtil.copyProperties(p, BaseUserPageResp.class);
            baseUserPageResp.setSerialNumber(serialNumber.getAndIncrement());
            contents.add(baseUserPageResp);
        });

        return new PageResult<BaseUserPageResp>((long)users.size(),
                1L,
                1L,
                (long)users.size(),
                contents
        );
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @Override
    public BaseUserDTO save(BaseUser user) {
        return baseUserMapper.toDto(baseUserRepository.save(user));
    }

    /**
     * 批量新增/修改用户
     * @param users 用户
     * @return
     */
    @Override
    public List<BaseUserDTO> saveAll(List<BaseUser> users) {
        return baseUserMapper.toDto(baseUserRepository.saveAll(users));
    }

    /**
     * 批量删除用户
     *
     * @param ids 被删除的用户id集合
     * @return true删除成功；false删除失败
     */
    @Override
    @Transactional
    public Boolean deleteByIds(List<Long> ids) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long realAppId = myAuthentication.getRealAppId();
        List<BaseUser> allById = baseUserRepository.findAllById(ids);
        allById.forEach(p -> AssertUtil.isEquals(realAppId, p.getRealAppId(), () -> ClientException.clientByForbidden().serverMessage("不能删除其它应用下的用户")));
        baseUserRepository.deleteAll(allById);
        return true;
    }

    /**
     * 重置用户密码
     *
     * @param userId 用户id
     * @return
     */
    @Override
    @Transactional
    public Boolean resetPassword(Long userId) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long realAppId = myAuthentication.getRealAppId();
        BaseUser user = this.findById(userId);
        AssertUtil.isEquals(realAppId, user.getRealAppId(), () -> ClientException.clientByForbidden().serverMessage("不能修改其它应用下的用户"));
        user.setPassword(passwordEncoder.encode("123456"));
        this.save(user);
        return true;
    }

    /**
     * 修改用户激活状态
     *
     * @param userId 用户id
     * @return
     */
    @Override
    @Transactional
    public Boolean changeEnabled(Long userId) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long realAppId = myAuthentication.getRealAppId();
        BaseUser user = this.findById(userId);
        AssertUtil.isEquals(realAppId, user.getRealAppId(), () -> ClientException.clientByForbidden().serverMessage("不能修改其它应用下的用户"));
        user.setEnabled(!user.getEnabled());
        this.save(user);
        return true;
    }

    /**
     * 修改用户锁定状态
     *
     * @param userId 用户id
     * @return
     */
    @Override
    @Transactional
    public Boolean changeLocked(Long userId) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long realAppId = myAuthentication.getRealAppId();
        BaseUser user = this.findById(userId);
        AssertUtil.isEquals(realAppId, user.getRealAppId(), () -> ClientException.clientByForbidden().serverMessage("不能修改其它应用下的用户"));
        user.setLocked(!user.getLocked());
        this.save(user);
        return true;
    }

}