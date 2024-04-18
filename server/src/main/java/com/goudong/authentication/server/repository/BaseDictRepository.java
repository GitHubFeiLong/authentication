package com.goudong.authentication.server.repository;

import com.goudong.authentication.server.domain.BaseDict;
import com.goudong.authentication.server.repository.resp.IdCountResp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;


/**
 * Spring Data  repository for the BaseDict entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaseDictRepository extends JpaRepository<BaseDict, Long>, JpaSpecificationExecutor<BaseDict> {

    /**
     * 查询字典下的配置数量
     * @param dictIds   字典ID集合
     * @return          字典及字典配置的数量
     */
    @Query(value = "select dict_id as id, count(0) as count from base_dict_setting where dict_id in (:dictIds) group by dict_id", nativeQuery = true)
    List<Map<String, BigInteger>> queryCount(List<Long> dictIds);

    /**
     * 根据应用ID和字典编码查询字典
     * @param appId 应用ID
     * @param code  字典编码
     * @return  应用对象
     */
    BaseDict findByAppIdAndCode(Long appId, String code);

    /**
     * 删除指定字典类型下的所有字典明细
     * @param dictTypeId    字典类型ID
     * @return  删除的数量
     */
    @Modifying
    @Query(value = "delete from base_dict where dict_type_id = :dictTypeId", nativeQuery = true)
    int deleteByDictTypeId(Long dictTypeId);
}
