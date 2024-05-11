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
    @Modifying
    @Query(nativeQuery = true, value ="update base_dict_setting set defaulted=0 where dict_id =?1")
    int updateNonDefaultedByDictId(Long dictId);

    /**
     * 删除指定字典类型下的所有字典配置
     * @param dictTypeId    字典类型ID
     * @return  删除的数量
     */
    @Modifying
    @Query(value = "delete from base_dict_setting where dict_type_id = :dictTypeId", nativeQuery = true)
    int deleteByDictTypeId(Long dictTypeId);

    /**
     * 删除指定字典明细下的所有字典配置
     * @param dictId    字典明细ID
     * @return  删除的数量
     */
    @Modifying
    @Query(value = "delete from base_dict_setting where dict_id = :dictId", nativeQuery = true)
    int deleteByDictId(Long dictId);

    /**
     * 修改字典下的所有配置都改为非默认
     * @param dictId    字典ID
     * @return  修改数量
     */
    @Query(value = "update base_dict_setting set defaulted = false where dict_id = :dictId and defaulted = true" , nativeQuery = true)
    @Modifying
    int updateDefaultedByDictId(Long dictId);
}
