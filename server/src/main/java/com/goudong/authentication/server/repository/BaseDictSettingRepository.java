package com.goudong.authentication.server.repository;

import com.goudong.authentication.server.domain.BaseDictSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BaseDictSetting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaseDictSettingRepository extends JpaRepository<BaseDictSetting, Long>, JpaSpecificationExecutor<BaseDictSetting> {

}
