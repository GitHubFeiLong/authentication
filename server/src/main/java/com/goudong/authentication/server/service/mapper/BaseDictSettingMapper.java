package com.goudong.authentication.server.service.mapper;

import com.goudong.authentication.server.domain.BaseDictSetting;
import com.goudong.authentication.server.service.dto.BaseDictSettingDTO;
import org.mapstruct.Mapper;

/**
 * 类描述：
 * 实体{@link BaseDictSetting}和它的DTO{@link BaseDictSettingDTO}的映射
 * @author chenf
 */
@Mapper(componentModel = "spring", uses = {})
public interface BaseDictSettingMapper extends EntityMapper<BaseDictSettingDTO, BaseDictSetting> {
}
