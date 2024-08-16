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
import java.util.ArrayList;
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
    public List<BankAccount> getListBankAccountByCustomerId(SearchBankAccountRQ searchRQ) {
        List<BankAccountEntity> bankAccountEntities = bankAccountRepository.searchBankAccount(searchRQ.getStatus(), searchRQ.getPartnerName(), searchRQ.getCustomerId());
        return bankAccountMapper.toDTOs(bankAccountEntities);
    }

    @Override
    public List<String> getListStatus() {
        List<String> listStatus = bankAccountRepository.findDistinctStatus();
        return listStatus;
    }

    @Override
    public List<ListPartnerInfo> getDistinctPartnerInfoByCustomer(Long customerId) {
        List<ListPartnerInfo> listPartnerInfo = bankAccountRepository.findDistinctPartnerInfo(customerId);
        return listPartnerInfo;
    }

    @Override
    public void updateBankAccountStatus() {
        OffsetDateTime now = OffsetDateTime.now();
        List<BankAccountEntity> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountEntity> bankAccountEntities = new ArrayList<>();

        for (BankAccountEntity bankAccount : bankAccounts) {
            String newStatus = determineStatus(bankAccount, now);
            if (newStatus != null && !newStatus.equals(bankAccount.getStatus())) {
                bankAccount.setStatus(newStatus);
                bankAccountEntities.add(bankAccount);
            }
        }
        bankAccountRepository.saveAll(bankAccountEntities);
    }


    private String determineStatus(BankAccountEntity bankAccount, OffsetDateTime now) {
        if (bankAccount.getToDate() != null && now.isAfter(bankAccount.getToDate())) {
            return "INACTIVE";
        } else if (bankAccount.getFromDate() != null && bankAccount.getToDate() != null
                && now.isBefore(bankAccount.getFromDate()) && now.isBefore(bankAccount.getToDate())) {
            return "REGISTERED";
        } else if (bankAccount.getFromDate() != null && bankAccount.getToDate() != null
                && !now.isBefore(bankAccount.getFromDate()) && !now.isAfter(bankAccount.getToDate())) {
            return "ACTIVE";
        }
        return null;
    }


}
