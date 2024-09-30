package com.goudong.authentication.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.authentication.common.util.AssertUtil;
import com.goudong.authentication.common.util.CollectionUtil;
import com.goudong.authentication.common.util.StringUtil;
import com.goudong.authentication.server.domain.BaseDictType;
import com.goudong.authentication.server.exception.ClientException;
import com.goudong.authentication.server.lang.PageResult;
import com.goudong.authentication.server.repository.BaseDictTypeRepository;
import com.goudong.authentication.server.rest.req.*;
import com.goudong.authentication.server.rest.resp.BaseDictTypePageResp;
import com.goudong.authentication.server.service.BaseDictTypeService;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.mapper.BaseDictTypeMapper;
import com.goudong.authentication.server.util.SecurityContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 类描述：
 * Service Implementation for managing {@link BaseDictType}.
 * @author chenf
 */
@Service
@Slf4j
public class BaseDictTypeServiceImpl implements BaseDictTypeService {

    //~fields
    //==================================================================================================================
    @Resource
    private BaseDictTypeRepository baseDictTypeRepository;

    @Resource
    private BaseDictTypeMapper baseDictTypeMapper;

    //~methods
    //==================================================================================================================
    /**
     * 分页字典类型
     *
     * @param req 分页参数
     * @return 分页结果
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<BaseDictTypePageResp> page(BaseDictTypePageReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        log.info("构造查询条件");
        Specification<BaseDictType> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> andPredicateList = new ArrayList<>();
            andPredicateList.add(criteriaBuilder.equal(root.get("appId"), myAuthentication.getRealAppId()));

            //1.获取比较的属性
            if (CollectionUtil.isNotEmpty(req.getIds())) {
                andPredicateList.add(root.<Long>get("id").in(req.getIds()));
            }
            if (StringUtil.isNotEmpty(req.getCode())) {
                andPredicateList.add(criteriaBuilder.like(root.get("code").as(String.class), "%" + req.getCode() + "%"));
            }
            if (StringUtil.isNotEmpty(req.getName())) {
                andPredicateList.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + req.getName() + "%"));
            }

            return criteriaBuilder.and(andPredicateList.toArray(new Predicate[0]));
        };

        // 单独创建排序对象
        Sort createdDateDesc = Sort.by("createdDate").descending();

        // 开启分页，就需要分页查询
        if (req.getOpenPage()) {
            log.info("使用分页查询");
            Pageable pageable = PageRequest.of(req.getPage(), req.getSize(), createdDateDesc);
            Page<BaseDictType> pageResult = baseDictTypeRepository.findAll(specification, pageable);

            log.info("查询每个字典类型下的子字典数量");
            List<Long> ids = pageResult.getContent().stream().map(BaseDictType::getId).collect(Collectors.toList());
            Map<Long, Integer> idCountMap = baseDictTypeRepository.queryCount(ids).stream().collect(Collectors.toMap(p-> p.get("id").longValue(), p -> p.get("count").intValue(), (k1, k2) -> k1));

            log.info("设置序号和字典数量");
            List<BaseDictTypePageResp> contents = BeanUtil.copyToList(pageResult.getContent(), BaseDictTypePageResp.class);
            AtomicLong serialNumber = new AtomicLong(req.getStartSerialNumber());

            contents.forEach(p -> {
                p.setSerialNumber(serialNumber.getAndIncrement());
                p.setDictNumber(idCountMap.containsKey(p.getId()) ? Optional.ofNullable(idCountMap.get(p.getId())).orElseGet(() -> 0) : 0);
            });

            return new PageResult<BaseDictTypePageResp>(pageResult.getTotalElements(),
                    (long)pageResult.getTotalPages(),
                    pageResult.getPageable().getPageNumber() + 1L,
                    (long)pageResult.getPageable().getPageSize(),
                    contents
            );
        }

        log.info("不使用分页查询");
        // 没开启分页，就需要查询所有
        List<BaseDictType> dictTypes = baseDictTypeRepository.findAll(specification, createdDateDesc);

        log.info("查询每个字典类型下的子字典数量");
        List<Long> ids = dictTypes.stream().map(BaseDictType::getId).collect(Collectors.toList());
        Map<Long, Integer> idCountMap = baseDictTypeRepository.queryCount(ids).stream().collect(Collectors.toMap(p-> p.get("id").longValue(), p -> p.get("count").intValue(), (k1, k2) -> k1));

        log.info("设置序号和字典数量");
        List<BaseDictTypePageResp> contents = BeanUtil.copyToList(dictTypes, BaseDictTypePageResp.class);
        AtomicLong serialNumber = new AtomicLong(req.getStartSerialNumber());
        contents.forEach(p -> {
            p.setSerialNumber(serialNumber.getAndIncrement());
            p.setDictNumber(idCountMap.containsKey(p.getId()) ? Optional.ofNullable(idCountMap.get(p.getId())).orElseGet(() -> 0) : 0);
        });

        return new PageResult<BaseDictTypePageResp>((long)dictTypes.size(),
                1L,
                1L,
                (long)dictTypes.size(),
                contents
        );
    }

    /**
     * 新增字典类型
     *
     * @param req 新增参数
     * @return 新增的结果
     */
    @Override
    public BaseDictType save(BaseDictType req) {
        log.info("保存字典类型");
        // 当请求没有传递参数时，设置默认值
        if (req.getEnabled() == null) {
            req.setEnabled(true);
        }
        if (StringUtil.isBlank(req.getTemplate())) {
            req.setTemplate("{}");
        }
        return baseDictTypeRepository.save(req);
    }

    /**
     * 根据ID查询字典类型
     *
     * @param id 字典类型ID
     * @return 字典类型
     */
    @Override
    public BaseDictType findById(Long id) {
        log.info("根据ID{}查询字典类型", id);
        MyAuthentication myAuthentication = SecurityContextUtil.get();

        Specification<BaseDictType> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> andPredicateList = new ArrayList<>();
            andPredicateList.add(criteriaBuilder.equal(root.get("appId"), myAuthentication.getRealAppId()));
            andPredicateList.add(criteriaBuilder.equal(root.get("id"), id));
            return criteriaBuilder.and(andPredicateList.toArray(new Predicate[andPredicateList.size()]));
        };
        return baseDictTypeRepository.findOne(specification).orElseThrow(() -> ClientException.client("字典类型不存在"));
    }

    /**
     * 修改字典类型
     *
     * @param req 修改参数
     * @return 修改后结果
     */
    @Override
    public BaseDictType update(BaseDictTypeUpdateReq req) {
        log.info("修改字典类型");
        BaseDictType baseDictType = this.findById(req.getId());
        BeanUtil.copyProperties(req, baseDictType);
        baseDictTypeRepository.save(baseDictType);
        return baseDictType;
    }

    /**
     * 修改字典类型的激活状态
     *
     * @param req 修改字典类型参数
     * @return true：修改成功；false：修改失败
     */
    @Override
    public Boolean changeEnabled(BaseDictTypeChangeEnabledReq req) {
        log.info("查询字典类型：{}", req.getId());
        BaseDictType dictType = this.findById(req.getId());
        log.info("修改字典类型激活状态：原enabled={},将要修改为{}", dictType.getEnabled(), !dictType.getEnabled());
        dictType.setEnabled(!dictType.getEnabled());
        baseDictTypeRepository.save(dictType);
        log.info("修改字典类型激活状态成功");
        return true;
    }

    /**
     * 批量删除字典类型
     *
     * @param ids 被删除的字典类型id集合
     * @return true删除成功；false删除失败
     */
    @Override
    @Transactional
    public Boolean deleteByIds(List<Long> ids) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long realAppId = myAuthentication.getRealAppId();
        List<BaseDictType> allById = baseDictTypeRepository.findAllById(ids);
        allById.forEach(p -> AssertUtil.isEquals(realAppId, p.getAppId(), () -> ClientException.clientByForbidden().clientMessage("权限不足，删除失败").serverMessage("不能删除其它应用下的字典类型")));
        baseDictTypeRepository.deleteAll(allById);
        return true;
    }

    /**
     * 条件分页查询字典类型下拉
     *
     * @param req 下拉分页参数
     * @return 字典类型下拉分页结果
     */
    @Override
    public PageResult<BaseDictTypeDropDownResp> baseDictTypeDropDown(BaseDictTypeDropDownReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        log.info("构造查询条件");
        Specification<BaseDictType> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> andPredicateList = new ArrayList<>();
            andPredicateList.add(criteriaBuilder.equal(root.get("appId"), myAuthentication.getRealAppId()));
            if (StringUtil.isNotBlank(req.getCode())) {
                andPredicateList.add(criteriaBuilder.like(root.get("code").as(String.class), "%" + req.getCode() + "%"));
            }
            return criteriaBuilder.and(andPredicateList.toArray(new Predicate[0]));
        };

        // 开启分页，就需要分页查询
        if (req.getOpenPage()) {
            log.info("使用分页查询");
            Sort sort = Sort.by("id").ascending();
            Pageable pageable = PageRequest.of(req.getPage(), req.getSize(), sort);
            Page<BaseDictType> pageResult = baseDictTypeRepository.findAll(specification, pageable);

            List<BaseDictTypeDropDownResp> respList = new ArrayList<>(pageResult.getContent().size());
            pageResult.getContent().forEach(p -> {
                respList.add(new BaseDictTypeDropDownResp(p.getId(), p.getCode(), p.getName()));
            });

            return new PageResult<BaseDictTypeDropDownResp>(pageResult.getTotalElements(),
                    (long)pageResult.getTotalPages(),
                    pageResult.getPageable().getPageNumber() + 1L,
                    (long)pageResult.getPageable().getPageSize(),
                    respList
            );
        }

        log.info("使用基本查询");
        Sort sort = Sort.by("id").ascending();
        List<BaseDictType> dictTypes = baseDictTypeRepository.findAll(specification, sort);

        List<BaseDictTypeDropDownResp> respList = new ArrayList<>(dictTypes.size());
        dictTypes.forEach(p -> {
            respList.add(new BaseDictTypeDropDownResp(p.getId(), p.getCode(), p.getName()));
        });

        return new PageResult<BaseDictTypeDropDownResp>((long)respList.size(),
                1L,
                1L,
                (long)respList.size(),
                respList
        );
    }
}
