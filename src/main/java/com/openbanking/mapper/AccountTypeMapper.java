package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.AccountEntity;
import com.openbanking.entity.AccountTypeEntity;
import com.openbanking.model.account.Account;
import com.openbanking.model.account.CreateAccount;
import com.openbanking.model.account.UpdateAccount;
import com.openbanking.model.account_type.AccountType;
import com.openbanking.model.account_type.CreateAccountType;
import com.openbanking.model.account_type.UpdateAccountType;
import org.mapstruct.Mapper;

@Mapper
public interface AccountTypeMapper extends BaseMapper<AccountTypeEntity, AccountType, CreateAccountType, UpdateAccountType> {
}
