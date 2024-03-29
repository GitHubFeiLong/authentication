package com.goudong.authentication.server.service.mapper;

import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.domain.BaseRole;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.authentication.server.service.dto.BaseRoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 类描述：
 * 实体{@link BaseRole}和它的DTO{@link BaseRoleDTO}的映射
 * @author chenf
 */
@Mapper(componentModel = "spring", uses = {})
public interface BaseRoleMapper extends EntityMapper<BaseRoleDTO, BaseRole> {


    @Mapping(target = "users", ignore = true)
    @Mapping(target = "menus", ignore = true)
    BaseRole toEntity(BaseRoleDTO baseRoleDTO);

    default BaseRole fromId(Long id) {
        if (id == null) {
            return null;
        }
        BaseRole baseRole = new BaseRole();
        baseRole.setId(id);
        return baseRole;
    }
}
