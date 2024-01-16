package com.goudong.authentication.server.service.mapper;

import com.goudong.authentication.server.domain.BaseRoleMenu;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.service.dto.BaseRoleMenuDTO;
import com.goudong.authentication.server.service.dto.BaseUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 类描述：
 * 实体{@link BaseUser}和它的DTO{@link BaseUserDTO}的映射
 * @author chenf
 */
@Mapper(componentModel = "spring", uses = {})
public interface BaseUserMapper extends EntityMapper<BaseUserDTO, BaseUser> {


    @Override
    @Mapping(target = "roles", ignore = true)
    BaseUser toEntity(BaseUserDTO baseUserDTO);

    default BaseUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        BaseUser baseUser = new BaseUser();
        baseUser.setId(id);
        return baseUser;
    }
}
