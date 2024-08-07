package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.comon.SearchCriteria;
import com.openbanking.model.Account;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService extends BaseService<Account , Long> {
    Account create(Account dto);

    Account update(Long id, Account dto);

    Account getById(Long id);

    List<Account> getAll(SearchCriteria searchCriteria);

    void deleteById(Long id);
}
