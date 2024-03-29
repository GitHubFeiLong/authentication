package com.goudong.authentication.server.repository;

import com.goudong.authentication.server.domain.BaseDictType;
import com.goudong.authentication.server.repository.resp.IdCountResp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the BaseDict entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaseDictTypeRepository extends JpaRepository<BaseDictType, Long>, JpaSpecificationExecutor<BaseDictType> {

    /**
     * 查询字典类型下的字典数量
     * @param dictTypeIds   字典类型ID集合
     * @return              字典类型及类型下的字典数量
     */
    @Query(value = "select dict_type_id as id, count(0) as count from base_dict where dict_type_id in (:dictTypeIds) group by dict_type_id", nativeQuery = true)
    List<IdCountResp> queryCount(List<Long> dictTypeIds);
}
