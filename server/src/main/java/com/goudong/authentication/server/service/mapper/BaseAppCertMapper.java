package com.goudong.authentication.server.service.mapper;

import com.goudong.authentication.server.domain.BaseAppCert;
import com.goudong.authentication.server.service.dto.BaseAppCertDTO;
import org.mapstruct.Mapper;

/**
 * 类描述：
 * 实体{@link BaseAppCert}和它的DTO{@link BaseAppCertDTO}的映射
 * @author chenf
 */
@Mapper(componentModel = "spring", uses = {})
public interface BaseAppCertMapper extends EntityMapper<BaseAppCertDTO, BaseAppCert>  {
}
