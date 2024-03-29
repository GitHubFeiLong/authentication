package com.goudong.authentication.server.service.mapper;

import com.goudong.authentication.server.domain.BaseDict;
import com.goudong.authentication.server.domain.BaseDictType;
import com.goudong.authentication.server.service.dto.BaseDictDTO;
import com.goudong.authentication.server.service.dto.BaseDictTypeDTO;
import org.mapstruct.Mapper;

/**
 * 类描述：
 * 实体{@link BaseDict}和它的DTO{@link BaseDictDTO}的映射
 * @author chenf
 */
@Mapper(componentModel = "spring", uses = {})
public interface BaseDictMapper extends EntityMapper<BaseDictDTO, BaseDict> {
}
