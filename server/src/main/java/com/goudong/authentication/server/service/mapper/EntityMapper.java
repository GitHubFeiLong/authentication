package com.goudong.authentication.server.service.mapper;

import java.util.List;

/**
 * 类描述：
 * 为泛型dto到entity映射器订立契约。
 * @param <D> - DTO type parameter.
 * @param <E> - Entity type parameter.
 * @author chenf
 */
public interface EntityMapper <D, E> {

    /**
     * dto转entity
     * @param dto   dto
     * @return entity
     */
    E toEntity(D dto);

    /**
     * entity转dto
     * @param entity    entity
     * @return dto
     */
    D toDto(E entity);

    /**
     * dto集合转entity集合
     * @param dtoList   dto集合
     * @return entity集合
     */
    List <E> toEntity(List<D> dtoList);

    /**
     * entity集合转dto集合
     * @param entityList    entity集合
     * @return dto集合
     */
    List <D> toDto(List<E> entityList);
}
