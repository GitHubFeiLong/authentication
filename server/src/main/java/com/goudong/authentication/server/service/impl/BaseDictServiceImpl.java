package com.goudong.authentication.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.authentication.server.domain.BaseDict;
import com.goudong.authentication.server.domain.BaseDictType;
import com.goudong.authentication.server.repository.BaseDictRepository;
import com.goudong.authentication.server.repository.resp.IdCountResp;
import com.goudong.authentication.server.rest.req.BaseDictChangeEnabledReq;
import com.goudong.authentication.server.rest.req.BaseDictPageReq;
import com.goudong.authentication.server.rest.req.BaseDictUpdateReq;
import com.goudong.authentication.server.rest.resp.BaseDictPageResp;
import com.goudong.authentication.server.rest.resp.BaseDictTypePageResp;
import com.goudong.authentication.server.service.BaseDictService;
import com.goudong.authentication.server.service.dto.BaseDictDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.mapper.BaseDictMapper;
import com.goudong.authentication.server.util.SecurityContextUtil;
import com.goudong.boot.web.core.ClientException;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.CollectionUtil;
import com.goudong.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 类描述：
 * Service Implementation for managing {@link BaseDict}.
 * @author chenf
 */
@Service
@Slf4j
public class BaseDictServiceImpl implements BaseDictService {


    //~fields
    //==================================================================================================================
    /**
     * 字典持久层接口
     */
    @Resource
    private BaseDictRepository baseDictRepository;

    /**
     * 字典对象映射
     */
    @Resource
    private BaseDictMapper baseDictMapper;

    //~methods
    //==================================================================================================================
    /**
     * 分页查询字典
     *
     * @param req 分页查询字典参数
     * @return 分页字典
     */
    @Override
    public PageResult<BaseDictPageResp> page(BaseDictPageReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        log.info("构造查询条件");
        Specification<BaseDict> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> andPredicateList = new ArrayList<>();
            andPredicateList.add(criteriaBuilder.equal(root.get("appId"), myAuthentication.getRealAppId()));

            if (CollectionUtil.isNotEmpty(req.getIds())) {
                andPredicateList.add(root.<Long>get("id").in(req.getIds()));
            }
            if (req.getDictTypeId() != null) {
                andPredicateList.add(criteriaBuilder.equal(root.<Long>get("dictTypeId"), req.getDictTypeId()));
            }
            if (StringUtil.isNotBlank(req.getCode())) {
                andPredicateList.add(criteriaBuilder.like(root.get("code").as(String.class), "%" + req.getCode() + "%"));
            }
            if (StringUtil.isNotBlank(req.getName())) {
                andPredicateList.add(criteriaBuilder.like( root.get("name").as(String.class), "%" + req.getName() + "%"));
            }

            return criteriaBuilder.and(andPredicateList.toArray(new Predicate[0]));
        };

        // 单独创建排序对象
        Sort createdDateDesc = Sort.by("createdDate").descending();

        // 开启分页，就需要分页查询
        if (req.getOpenPage()) {
            log.info("使用分页查询");
            Pageable pageable = PageRequest.of(req.getPage(), req.getSize(), createdDateDesc);
            Page<BaseDict> pageResult = baseDictRepository.findAll(specification, pageable);

            log.info("设置序号");
            List<BaseDictPageResp> contents = BeanUtil.copyToList(pageResult.getContent(), BaseDictPageResp.class);
            AtomicLong serialNumber = new AtomicLong(req.getStartSerialNumber());

            contents.forEach(p -> {
                p.setSerialNumber(serialNumber.getAndIncrement());
            });

            return new PageResult<BaseDictPageResp>(pageResult.getTotalElements(),
                    (long)pageResult.getTotalPages(),
                    pageResult.getPageable().getPageNumber() + 1L,
                    (long)pageResult.getPageable().getPageSize(),
                    contents
            );
        }

        log.info("不使用分页查询");
        // 没开启分页，就需要查询所有
        List<BaseDict> dictTypes = baseDictRepository.findAll(specification, createdDateDesc);

        log.info("设置序号");
        List<BaseDictPageResp> contents = BeanUtil.copyToList(dictTypes, BaseDictPageResp.class);
        AtomicLong serialNumber = new AtomicLong(req.getStartSerialNumber());
        contents.forEach(p -> {
            p.setSerialNumber(serialNumber.getAndIncrement());
        });

        return new PageResult<BaseDictPageResp>((long)dictTypes.size(),
                1L,
                1L,
                (long)dictTypes.size(),
                contents
        );
    }

    /**
     * 新增字典
     *
     * @param baseDict 新增的字典参数
     * @return 新增的字典
     */
    @Override
    public BaseDict save(BaseDict baseDict) {
        log.info("保存字典");
        return baseDictRepository.save(baseDict);
    }

    /**
     * 根据主键查询字典
     *
     * @param id 字典主键
     * @return 字典
     */
    @Override
    public BaseDict findById(Long id) {
        log.info("根据ID{}查询字典", id);
        MyAuthentication myAuthentication = SecurityContextUtil.get();

        Specification<BaseDict> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> andPredicateList = new ArrayList<>();
            andPredicateList.add(criteriaBuilder.equal(root.get("appId"), myAuthentication.getRealAppId()));
            andPredicateList.add(criteriaBuilder.equal(root.get("id"), id));
            return criteriaBuilder.and(andPredicateList.toArray(new Predicate[andPredicateList.size()]));
        };
        return baseDictRepository.findOne(specification).orElseThrow(() -> ClientException.client("字典不存在"));
    }

    /**
     * 修改字典
     *
     * @param req 修改字典参数
     * @return 修改后的字典
     */
    @Override
    public BaseDict update(BaseDictUpdateReq req) {
        log.info("修改字典");
        BaseDict baseDict = this.findById(req.getId());
        BeanUtil.copyProperties(req, baseDict);
        baseDictRepository.save(baseDict);
        return baseDict;
    }

    /**
     * 修改字典的激活状态
     *
     * @param req 修改字典参数
     * @return true：修改成功；false：修改失败
     */
    @Override
    public Boolean changeEnabled(BaseDictChangeEnabledReq req) {
        log.info("查询字典：{}", req.getId());
        BaseDict dict = this.findById(req.getId());
        log.info("修改字典激活状态：原enabled={},将要修改为{}", dict.getEnabled(), !dict.getEnabled());
        dict.setEnabled(!dict.getEnabled());
        baseDictRepository.save(dict);
        log.info("修改字典激活状态成功");
        return true;
    }

    /**
     * 批量删除字典
     *
     * @param ids 字典主键集合
     * @return true：删除成功；false：删除失败
     */
    @Override
    public Boolean deleteByIds(List<Long> ids) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long realAppId = myAuthentication.getRealAppId();
        List<BaseDict> allById = baseDictRepository.findAllById(ids);
        allById.forEach(p -> AssertUtil.isEquals(realAppId, p.getAppId(), () -> ClientException.clientByForbidden().clientMessage("权限不足，删除失败").serverMessage("不能删除其它应用下的字典")));
        baseDictRepository.deleteAll(allById);
        return true;
    }
}
