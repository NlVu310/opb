package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.AccountEntity;
import com.openbanking.model.Account;
import org.mapstruct.Mapper;

@Mapper
public interface AccountMapper extends BaseMapper<AccountEntity, Account> {
}
