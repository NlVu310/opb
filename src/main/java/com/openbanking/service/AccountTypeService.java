package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.model.account_type.AccountType;
import com.openbanking.model.account_type.CreateAccountType;
import com.openbanking.model.account_type.UpdateAccountType;

import java.util.List;

public interface AccountTypeService extends BaseService<AccountType, CreateAccountType, UpdateAccountType, Long> {
    List<AccountType> getListAccountTypeById(Long id);
    void create(CreateAccountType createAccountType);

}
