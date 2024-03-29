package com.goudong.authentication.server.service.mapper;

import com.goudong.authentication.server.domain.BaseDictType;
import com.goudong.authentication.server.service.dto.BaseDictTypeDTO;
import org.mapstruct.Mapper;

/**
 * 类描述：
 * 实体{@link BaseDictType}和它的DTO{@link BaseDictTypeDTO}的映射
 * @author chenf
 */
@Mapper(componentModel = "spring", uses = {})
public interface BaseDictTypeMapper extends EntityMapper<BaseDictTypeDTO, BaseDictType> {
}
