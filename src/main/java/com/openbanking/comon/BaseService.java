package com.openbanking.comon;


import java.util.List;

public interface BaseService<T, CD, UD, ID> {
    T create(CD dto);

    T update(ID id, T dto);

    T getById(ID id);

    List<T> getAll(SearchCriteria searchCriteria);

    void deleteByListId(List<ID> ids);
}
