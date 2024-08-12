package com.openbanking.comon;

import com.openbanking.comon.BaseMapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseMapperImpl<E, D, CD, UD> implements BaseMapper<E, D, CD, UD> {

    @Override
    public List<D> toDTOs(List<E> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<E> toEntities(List<D> dtos) {
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
