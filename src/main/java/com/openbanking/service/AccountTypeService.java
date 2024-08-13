package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.comon.PaginationRS;
import com.openbanking.comon.SearchCriteria;
import com.openbanking.model.account_type.*;

public interface AccountTypeService extends BaseService<AccountType, CreateAccountType, UpdateAccountType, Long> {
    PaginationRS<AccountTypeInfo> getListAccountTypeByAccountId(Long id, SearchAccountTypeRQ searchCriteria);
    void create(CreateAccountType createAccountType);

    void update(UpdateAccountType updateAccountType);

    void deleteById(Long id);

    AccountTypeDetail getAccountTypeDetail(Long id);
}
