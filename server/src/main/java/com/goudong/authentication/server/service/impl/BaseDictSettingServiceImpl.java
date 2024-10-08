package com.goudong.authentication.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.authentication.common.util.AssertUtil;
import com.goudong.authentication.common.util.CollectionUtil;
import com.goudong.authentication.common.util.StringUtil;
import com.goudong.authentication.server.domain.BaseDictSetting;
import com.goudong.authentication.server.exception.ClientException;
import com.goudong.authentication.server.lang.PageResult;
import com.goudong.authentication.server.repository.BaseDictSettingRepository;
import com.goudong.authentication.server.rest.req.BaseDictSettingChangeEnabledReq;
import com.goudong.authentication.server.rest.req.BaseDictSettingPageReq;
import com.goudong.authentication.server.rest.req.BaseDictSettingUpdateReq;
import com.goudong.authentication.server.rest.resp.BaseDictSettingPageResp;
import com.goudong.authentication.server.service.BaseDictSettingService;
import com.goudong.authentication.server.service.dto.BaseDictSettingDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.mapper.BaseDictSettingMapper;
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
import java.util.concurrent.atomic.AtomicLong;

/**
 * 类描述：
 * Service Implementation for managing {@link BaseDictSetting}.
 * @author chenf
 */
@Service
@Slf4j
public class BaseDictSettingServiceImpl implements BaseDictSettingService {

    //~fields
    //==================================================================================================================
    /**
     * 字典配置持久层接口
     */
    @Resource
    private BaseDictSettingRepository baseDictSettingRepository;

    /**
     * 字典配置PO，DTO映射器
     */
    @Resource
    private BaseDictSettingMapper baseDictSettingMapper;

    //~methods
    //==================================================================================================================
    /**
     * 分页字典配置
     *
     * @param req 分页字典配置参数
     * @return 分页字典配置结果
     */
    @Override
    public PageResult<BaseDictSettingPageResp> page(BaseDictSettingPageReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        log.info("构造查询条件");
        Specification<BaseDictSetting> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> andPredicateList = new ArrayList<>();
            andPredicateList.add(criteriaBuilder.equal(root.get("appId"), myAuthentication.getRealAppId()));

            if (CollectionUtil.isNotEmpty(req.getIds())) {
                andPredicateList.add(root.<Long>get("id").in(req.getIds()));
            }
            if (req.getDictTypeId() != null) {
                andPredicateList.add(criteriaBuilder.equal(root.<Long>get("dictTypeId"), req.getDictTypeId()));
            }
            if (req.getDictId() != null) {
                andPredicateList.add(criteriaBuilder.equal(root.<Long>get("dictId"), req.getDictId()));
            }
            if (StringUtil.isNotBlank(req.getName())) {
                andPredicateList.add(criteriaBuilder.like(root.<String>get("name"), "%" + req.getName() + "%"));
            }

            return criteriaBuilder.and(andPredicateList.toArray(new Predicate[0]));
        };

        // 单独创建排序对象
        Sort sort = Sort.by("createdDate").descending();

        // 开启分页，就需要分页查询
        if (req.getOpenPage()) {
            log.info("使用分页查询");
            Pageable pageable = PageRequest.of(req.getPage(), req.getSize(), sort);
            Page<BaseDictSetting> pageResult = baseDictSettingRepository.findAll(specification, pageable);

            log.info("设置序号");
            List<BaseDictSettingPageResp> contents = BeanUtil.copyToList(pageResult.getContent(), BaseDictSettingPageResp.class);
            AtomicLong serialNumber = new AtomicLong(req.getStartSerialNumber());

            contents.forEach(p -> {
                p.setSerialNumber(serialNumber.getAndIncrement());
            });

            return new PageResult<BaseDictSettingPageResp>(pageResult.getTotalElements(),
                    (long)pageResult.getTotalPages(),
                    pageResult.getPageable().getPageNumber() + 1L,
                    (long)pageResult.getPageable().getPageSize(),
                    contents
            );
        }

        log.info("不使用分页查询");
        // 没开启分页，就需要查询所有
        List<BaseDictSetting> dictTypes = baseDictSettingRepository.findAll(specification, sort);

        log.info("设置序号");
        List<BaseDictSettingPageResp> contents = BeanUtil.copyToList(dictTypes, BaseDictSettingPageResp.class);
        AtomicLong serialNumber = new AtomicLong(req.getStartSerialNumber());
        contents.forEach(p -> {
            p.setSerialNumber(serialNumber.getAndIncrement());
        });

        return new PageResult<BaseDictSettingPageResp>((long)dictTypes.size(),
                1L,
                1L,
                (long)dictTypes.size(),
                contents
        );
    }

    /**
     * 根据字典配置主键查询字典配置
     *
     * @param id 字典配置主键
     * @return 字典配置
     */
    @Override
    public BaseDictSetting findById(Long id) {
        log.info("根据ID{}查询字典", id);
        MyAuthentication myAuthentication = SecurityContextUtil.get();

        Specification<BaseDictSetting> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> andPredicateList = new ArrayList<>();
            andPredicateList.add(criteriaBuilder.equal(root.get("appId"), myAuthentication.getRealAppId()));
            andPredicateList.add(criteriaBuilder.equal(root.get("id"), id));
            return criteriaBuilder.and(andPredicateList.toArray(new Predicate[andPredicateList.size()]));
        };
        return baseDictSettingRepository.findOne(specification).orElseThrow(() -> ClientException.client("字典配置不存在"));
    }

    /**
     * 修改字典配置
     *
     * @param req 修改参数
     * @return 修改后字典配置
     */
    @Override
    public BaseDictSetting update(BaseDictSettingUpdateReq req) {
        log.info("修改字典");
        BaseDictSetting dictSetting = this.findById(req.getId());
        BeanUtil.copyProperties(req, dictSetting);
        baseDictSettingRepository.save(dictSetting);
        return dictSetting;
    }

    /**
     * 修改字典配置的激活状态
     *
     * @param req 修改字典配置参数
     * @return true：修改成功；false：修改失败
     */
    @Override
    @Transactional
    public Boolean changeEnabled(BaseDictSettingChangeEnabledReq req) {
        log.info("查询字典配置：{}", req.getId());
        BaseDictSetting dictSetting = this.findById(req.getId());
        log.info("修改字典明细的其它字典配置为未激活");
        baseDictSettingRepository.updateEnabledByDictId(false, dictSetting.getDictId());
        log.info("修改字典配置激活状态：原enabled={},将要修改为{}", dictSetting.getEnabled(), !dictSetting.getEnabled());
        dictSetting.setEnabled(!dictSetting.getEnabled());
        baseDictSettingRepository.save(dictSetting);
        log.info("修改字典配置激活状态成功");
        return true;
    }

    /**
     * 批量删除字典配置
     *
     * @param ids 字典配置的主键
     * @return true：删除成功
     */
    @Override
    public Boolean deleteByIds(List<Long> ids) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long realAppId = myAuthentication.getRealAppId();
        List<BaseDictSetting> allById = baseDictSettingRepository.findAllById(ids);
        allById.forEach(p -> AssertUtil.isEquals(realAppId, p.getAppId(), () -> ClientException.clientByForbidden().clientMessage("权限不足，删除失败").serverMessage("不能删除其它应用下的字典配置")));
        baseDictSettingRepository.deleteAll(allById);
        return true;
    }

    /**
     * 批量删除字典配置
     *
     * @param dictTypeIds 字典类型ID集合
     * @return true：删除成功；false：删除失败
     */
    @Override
    @Transactional
    public boolean deleteByDictTypeIds(List<Long> dictTypeIds) {
        dictTypeIds.forEach(baseDictSettingRepository::deleteByDictTypeId);
        return true;
    }

    /**
     * 批量删除字典配置
     *
     * @param dictIds 字典明细ID集合
     * @return true：删除成功；false：删除失败
     */
    @Override
    @Transactional
    public boolean deleteByDictIds(List<Long> dictIds) {
        dictIds.forEach(baseDictSettingRepository::deleteByDictId);
        return true;
    }

    /**
     * 新增字典配置
     *
     * @param baseDictSetting 新增的字典配置参数
     * @return 新增的字典配置
     */
    @Override
    @Transactional
    public synchronized BaseDictSetting save(BaseDictSetting baseDictSetting) {
        log.info("保存字典配置");
        return baseDictSettingRepository.save(baseDictSetting);
    }

    /**
     * 根据字典主键ID查询激活状态的字典配置
     *
     * @param dictId 字典主键ID
     * @return 字典配置
     */
    @Override
    public BaseDictSettingDTO getBaseDictSettingByDictId(Long dictId) {
        BaseDictSetting first = baseDictSettingRepository.findFirstByDictIdAndEnabled(dictId, true);
        return baseDictSettingMapper.toDto(first);
    }
}
