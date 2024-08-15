package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.comon.PaginationRS;
import com.openbanking.entity.AccountEntity;
import com.openbanking.entity.AccountTypeEntity;
import com.openbanking.entity.BankAccountEntity;
import com.openbanking.mapper.BankAccountMapper;
import com.openbanking.model.account_type.AccountTypeInfo;
import com.openbanking.model.bank_account.*;
import com.openbanking.repository.BankAccountRepository;
import com.openbanking.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Override
    public List<String> findDistinctStatus() {
        List<String> listStatus  = bankAccountRepository.findDistinctStatus();
        return listStatus;
    }

    @Override
    public List<ListPartnerInfo> findDistinctPartnerInfo() {
        List<ListPartnerInfo> listPartnerInfo = bankAccountRepository.findDistinctPartnerInfo();
        return  listPartnerInfo;
    }

    @Override
    public PaginationRS<BankAccount> getListBankAccount(Long id , SearchBankAccountRQ searchRQ) {
        return null;
    }

}
