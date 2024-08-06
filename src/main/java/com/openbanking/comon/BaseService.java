package com.openbanking.comon;


import java.util.List;

public interface BaseService<T, ID> {
    T create(T dto);

    T update(ID id, T dto);

    T getById(ID id);

    List<T> getAll(SearchCriteria searchCriteria);

    void deleteById(ID id);
}
