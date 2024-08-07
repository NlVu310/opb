package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.model.account.Account;
import com.openbanking.model.account.CreateAccount;
import com.openbanking.model.account.UpdateAccount;
import com.openbanking.model.account_type.AccountType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService extends BaseService<Account, CreateAccount, UpdateAccount, Long> {
    Account create(CreateAccount dto);

//
//    Account update(Long id, Account dto);
//
//    Account getById(Long id);
//
//    List<Account> getAll(SearchCriteria searchCriteria);
//
//    void deleteById(Long id);
}
