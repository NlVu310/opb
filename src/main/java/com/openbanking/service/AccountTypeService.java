package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.comon.PaginationRS;
import com.openbanking.comon.SearchCriteria;
import com.openbanking.model.account_type.*;

import java.util.List;

public interface AccountTypeService extends BaseService<AccountType, CreateAccountType, UpdateAccountType, Long> {
    PaginationRS<AccountTypeInfo> getListAccountType(SearchAccountTypeRQ searchCriteria);
    void createAccountType(CreateAccountType createAccountType, Long accountId);

    void update(UpdateAccountType updateAccountType);

    void deleteById(Long id);

    AccountTypeDetail getAccountTypeDetail(Long id);

    List<AccountTypeInfo> getAccountTypeInfo();
}
