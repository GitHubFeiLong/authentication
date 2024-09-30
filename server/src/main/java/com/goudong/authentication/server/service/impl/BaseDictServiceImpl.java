package com.goudong.authentication.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.authentication.common.util.AssertUtil;
import com.goudong.authentication.common.util.CollectionUtil;
import com.goudong.authentication.common.util.StringUtil;
import com.goudong.authentication.server.domain.BaseDict;
import com.goudong.authentication.server.exception.ClientException;
import com.goudong.authentication.server.exception.DictNotFoundException;
import com.goudong.authentication.server.lang.PageResult;
import com.goudong.authentication.server.repository.BaseDictRepository;
import com.goudong.authentication.server.rest.req.*;
import com.goudong.authentication.server.rest.resp.BaseDictPageResp;
import com.goudong.authentication.server.service.BaseDictService;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.mapper.BaseDictMapper;
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

            log.info("查询每个字典明细下的字典配置数量");
            List<Long> ids = pageResult.getContent().stream().map(BaseDict::getId).collect(Collectors.toList());
            Map<Long, Integer> idCountMap = baseDictRepository.queryCount(ids).stream().collect(Collectors.toMap(p-> p.get("id").longValue(), p -> p.get("count").intValue(), (k1, k2) -> k1));
            log.info("设置序号和字典配置数量");
            List<BaseDictPageResp> contents = BeanUtil.copyToList(pageResult.getContent(), BaseDictPageResp.class);
            AtomicLong serialNumber = new AtomicLong(req.getStartSerialNumber());
            contents.forEach(p -> {
                p.setSerialNumber(serialNumber.getAndIncrement());
                p.setDictSettingNumber(idCountMap.containsKey(p.getId()) ? Optional.ofNullable(idCountMap.get(p.getId())).orElseGet(() -> 0) : 0);
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

        List<Long> ids = dictTypes.stream().map(BaseDict::getId).collect(Collectors.toList());
        Map<Long, Integer> idCountMap = baseDictRepository.queryCount(ids).stream().collect(Collectors.toMap(p-> p.get("id").longValue(), p -> p.get("count").intValue(), (k1, k2) -> k1));
        log.info("设置序号和字典配置数量");
        List<BaseDictPageResp> contents = BeanUtil.copyToList(dictTypes, BaseDictPageResp.class);
        AtomicLong serialNumber = new AtomicLong(req.getStartSerialNumber());
        contents.forEach(p -> {
            p.setSerialNumber(serialNumber.getAndIncrement());
            p.setDictSettingNumber(idCountMap.containsKey(p.getId()) ? Optional.ofNullable(idCountMap.get(p.getId())).orElseGet(() -> 0) : 0);
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
        if (baseDict.getEnabled() == null) {
            baseDict.setEnabled(true);
        }
        if (StringUtil.isBlank(baseDict.getTemplate())) {
            baseDict.setTemplate("{}");
        }
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
        BaseDict baseDict = baseDictRepository.findOne(specification).orElseThrow(() -> ClientException.client("字典不存在"));
        return baseDict;
    }

    /**
     * 根据应用ID和字典编码查询字典信息
     *
     * @param appId 应用ID
     * @param code  字典编码
     * @return 字典对象
     * @throws DictNotFoundException 字典不存在
     */
    @Override
    public BaseDict findByAppIdAndCode(Long appId, String code) {
        BaseDict baseDict = baseDictRepository.findByAppIdAndCode(appId, code);
        return Optional.ofNullable(baseDict).orElseThrow(() -> new DictNotFoundException("字典不存在"));
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

    /**
     * 批量删除字典
     *
     * @param dictTypeIds 字典类型ID集合
     * @return true：删除成功；false：删除失败
     */
    @Override
    @Transactional
    public Boolean deleteByDictTypeIds(List<Long> dictTypeIds) {
        dictTypeIds.forEach(baseDictRepository::deleteByDictTypeId);
        return true;
    }

    /**
     * 条件分页查询字典明细下拉
     *
     * @param req 下拉分页参数
     * @return 字典明细下拉分页结果
     */
    @Override
    public PageResult<BaseDictDropDownResp> baseDictDropDown(BaseDictDropDownReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        log.info("构造查询条件");
        Specification<BaseDict> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> andPredicateList = new ArrayList<>();
            andPredicateList.add(criteriaBuilder.equal(root.get("appId"), myAuthentication.getRealAppId()));
            if (req.getDictTypeId() != null) {
                andPredicateList.add(criteriaBuilder.equal(root.get("dictTypeId"), req.getDictTypeId()));
            }
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
            Page<BaseDict> pageResult = baseDictRepository.findAll(specification, pageable);

            List<BaseDictDropDownResp> respList = new ArrayList<>(pageResult.getContent().size());
            pageResult.getContent().forEach(p -> {
                respList.add(new BaseDictDropDownResp(p.getId(), p.getCode(), p.getName()));
            });

            return new PageResult<BaseDictDropDownResp>(pageResult.getTotalElements(),
                    (long)pageResult.getTotalPages(),
                    pageResult.getPageable().getPageNumber() + 1L,
                    (long)pageResult.getPageable().getPageSize(),
                    respList
            );
        }

        log.info("使用基本查询");
        Sort sort = Sort.by("id").ascending();
        List<BaseDict> dictTypes = baseDictRepository.findAll(specification, sort);

        List<BaseDictDropDownResp> respList = new ArrayList<>(dictTypes.size());
        dictTypes.forEach(p -> {
            respList.add(new BaseDictDropDownResp(p.getId(), p.getCode(), p.getName()));
        });

        return new PageResult<BaseDictDropDownResp>((long)respList.size(),
                1L,
                1L,
                (long)respList.size(),
                respList
        );
    }
}
