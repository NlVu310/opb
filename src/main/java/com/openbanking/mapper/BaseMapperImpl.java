package com.openbanking.mapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseMapperImpl<E, D> implements BaseMapper<E, D> {

    @Override
    public List<D> toDTOs(List<E> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<E> toEntities(List<D> dtos) {
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
