package com.openbanking.comon;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;
public interface BaseMapper<E, D, CD, UD> {
    D toDTO(E entity);

    E toEntity(D dto);
    E toEntityFromCD(CD dto);

    void updateEntityFromDTO(UD dto, @MappingTarget E entity);

    List<D> toDTOs(List<E> entities);

    List<E> toEntities(List<D> dtos);
}

