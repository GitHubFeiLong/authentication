package com.goudong.authentication.server.service.mapper;

import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.domain.BaseUserRole;
import com.goudong.authentication.server.service.dto.BaseUserDTO;
import com.goudong.authentication.server.service.dto.BaseUserRoleDTO;
import org.mapstruct.Mapper;


/**
 * 类描述：
 * 实体{@link BaseUserRole}和它的DTO{@link BaseUserRoleDTO}的映射
 * @author chenf
 */
@Mapper(componentModel = "spring", uses = {BaseUserMapper.class, BaseRoleMapper.class})
public interface BaseUserRoleMapper extends EntityMapper<BaseUserRoleDTO, BaseUserRole> {

    // @Mapping(source = "user.id", target = "userId")
    // @Mapping(source = "role.id", target = "roleId")
    BaseUserRoleDTO toDto(BaseUserRole baseUserRole);

    // @Mapping(source = "userId", target = "user")
    // @Mapping(source = "roleId", target = "role")
    BaseUserRole toEntity(BaseUserRoleDTO baseUserRoleDTO);

    default BaseUserRole fromId(Long id) {
        if (id == null) {
            return null;
        }
        BaseUserRole baseUserRole = new BaseUserRole();
        baseUserRole.setId(id);
        return baseUserRole;
    }
}
