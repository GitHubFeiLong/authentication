package com.goudong.authentication.server.service.mapper;

import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


/**
 * 类描述：
 * 实体{@link BaseMenu}和它的DTO{@link BaseMenuDTO}的映射
 * @author chenf
 */
@Mapper(componentModel = "spring", uses = {})
public interface BaseMenuMapper extends EntityMapper<BaseMenuDTO, BaseMenu> {


    @Mapping(target = "roles", ignore = true)
    BaseMenu toEntity(BaseMenuDTO baseMenuDTO);

    default BaseMenu fromId(Long id) {
        if (id == null) {
            return null;
        }
        BaseMenu baseMenu = new BaseMenu();
        baseMenu.setId(id);
        return baseMenu;
    }
}
