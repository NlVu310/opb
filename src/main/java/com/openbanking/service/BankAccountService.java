package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.model.bank_account.*;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface BankAccountService  extends BaseService<BankAccount, CreateBankAccount, UpdateBankAccount, Long> {

    List<BankAccount> getListBankAccountByCustomerId(SearchBankAccountRQ searchRQ);

    List<String> getListStatus();

    List<ListPartnerInfo> getDistinctPartnerInfoByCustomer(Long customerId);

    void updateBankAccountStatus();

}
