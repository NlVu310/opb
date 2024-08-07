package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.AccountEntity;
import com.openbanking.entity.AccountTypeEntity;
import com.openbanking.mapper.AccountMapper;
import com.openbanking.mapper.AccountTypeMapper;
import com.openbanking.model.account.Account;
import com.openbanking.model.account.CreateAccount;
import com.openbanking.model.account.UpdateAccount;
import com.openbanking.model.account_type.AccountType;
import com.openbanking.repository.AccountRepository;
import com.openbanking.repository.AccountTypeRepository;
import com.openbanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl extends BaseServiceImpl<AccountEntity, Account, CreateAccount, UpdateAccount, Long> implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private AccountTypeMapper accountTypeMapper;
    @Autowired
    private AccountTypeRepository accountTypeRepository;



    public AccountServiceImpl(BaseRepository<AccountEntity, Long> repository, BaseMapper<AccountEntity, Account, CreateAccount, UpdateAccount> mapper) {
        super(repository, mapper);
    }


}
