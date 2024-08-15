package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.comon.PaginationRS;
import com.openbanking.model.bank_account.*;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface BankAccountService  extends BaseService<BankAccount, CreateBankAccount, UpdateBankAccount, Long> {
    PaginationRS<BankAccount> getListBankAccount(Long id , SearchBankAccountRQ searchCriteria);

    List<BankAccount> getListBankAccountByCustomerId(SearchBankAccountRQ searchRQ);

    List<String> findDistinctStatus();

    List<ListPartnerInfo> getDistinctPartnerInfoByCustomer(Long customerId);

}
