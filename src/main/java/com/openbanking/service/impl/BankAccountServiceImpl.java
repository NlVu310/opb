package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.BankAccountEntity;
import com.openbanking.mapper.BankAccountMapper;
import com.openbanking.model.bank_account.BankAccount;
import com.openbanking.model.bank_account.CreateBankAccount;
import com.openbanking.model.bank_account.UpdateBankAccount;
import com.openbanking.repository.BankAccountRepository;
import com.openbanking.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountServiceImpl extends BaseServiceImpl<BankAccountEntity, BankAccount, CreateBankAccount, UpdateBankAccount, Long> implements BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private BankAccountMapper bankAccountMapper;
    public BankAccountServiceImpl(BaseRepository<BankAccountEntity, Long> repository, BaseMapper<BankAccountEntity, BankAccount, CreateBankAccount, UpdateBankAccount> mapper) {
        super(repository, mapper);
    }

    @Override
    public List<BankAccount> getListBankAccountByCustomerId(Long id) {
        List<BankAccountEntity> bankAccountEntities  = bankAccountRepository.getListBankAccountByCustomerId(id);
        return bankAccountMapper.toDTOs(bankAccountEntities);
    }
}
