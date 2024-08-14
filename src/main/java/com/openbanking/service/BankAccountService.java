package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.model.bank_account.BankAccount;
import com.openbanking.model.bank_account.CreateBankAccount;
import com.openbanking.model.bank_account.UpdateBankAccount;
import com.openbanking.model.customer.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface BankAccountService  extends BaseService<BankAccount, CreateBankAccount, UpdateBankAccount, Long> {
    List<BankAccount> getListBankAccountByCustomerId(Long id);
}
