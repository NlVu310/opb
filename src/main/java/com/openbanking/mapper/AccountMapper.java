package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.AccountEntity;
import com.openbanking.model.account.Account;
import com.openbanking.model.account.CreateAccount;
import com.openbanking.model.account.UpdateAccount;
import org.mapstruct.Mapper;

@Mapper
public interface AccountMapper extends BaseMapper<AccountEntity, Account, CreateAccount, UpdateAccount> {
}
