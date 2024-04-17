package com.goudong.authentication.server.repository;

import com.goudong.authentication.server.domain.BaseDictSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * Spring Data  repository for the BaseDictSetting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaseDictSettingRepository extends JpaRepository<BaseDictSetting, Long>, JpaSpecificationExecutor<BaseDictSetting> {

    //改

    /**
     * 将字典下所有配置都设置称非默认
     * @param dictId    字典ID
     * @return  修改数量
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value ="update base_dict_setting set defaulted=0 where dict_id =?1")
    int updateNonDefaultedByDictId(Long dictId);
}
