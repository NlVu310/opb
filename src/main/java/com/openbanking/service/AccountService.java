package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.comon.PaginationRS;
import com.openbanking.model.account.Account;
import com.openbanking.model.account.CreateAccount;
import com.openbanking.model.account.SearchAccountRQ;
import com.openbanking.model.account.UpdateAccount;
import org.springframework.stereotype.Service;

@Service
public interface AccountService extends BaseService<Account, CreateAccount, UpdateAccount, Long> {
//    Account create(CreateAccount dto);
    void resetPassword(Long id);

//
//    Account update(Long id, Account dto);
//
//    Account getById(Long id);
//
    PaginationRS<Account> getAll(SearchAccountRQ rq);
//
//    void deleteById(Long id);
}
