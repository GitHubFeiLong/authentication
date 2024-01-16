package com.goudong.authentication.server.service.mapper;

import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.service.dto.BaseAppDTO;
import org.mapstruct.Mapper;


/**
 * 类描述：
 * 实体{@link BaseApp}和它的DTO{@link BaseAppDTO}的映射
 * @author chenf
 */
@Mapper(componentModel = "spring", uses = {})
public interface BaseAppMapper extends EntityMapper<BaseAppDTO, BaseApp> {

    default BaseApp fromId(Long id) {
        if (id == null) {
            return null;
        }
        BaseApp baseApp = new BaseApp();
        baseApp.setId(id);
        return baseApp;
    }
}
