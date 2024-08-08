package com.openbanking.comon;


import java.util.List;

public interface BaseService<T, CD, UD, ID> {
    T create(CD dto, Long accountId);

    T update(UD dto, Long accountId);

    T getById(ID id);

    PaginationRS<T> getAll(SearchCriteria searchCriteria);

    void deleteByListId(List<ID> ids);
}
