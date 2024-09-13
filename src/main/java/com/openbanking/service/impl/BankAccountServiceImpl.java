package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;

import com.openbanking.entity.BankAccountEntity;
import com.openbanking.enums.BankAccountStatus;
import com.openbanking.mapper.BankAccountMapper;
import com.openbanking.model.account_type.AccountTypeInfo;
import com.openbanking.model.bank_account.*;
import com.openbanking.repository.BankAccountRepository;
import com.openbanking.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
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
        if (searchRQ == null) searchRQ = new SearchBankAccountRQ();
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
    @Transactional
    public void updateBankAccountStatus() {
        LocalDate now = LocalDate.now();

        List<BankAccountEntity> bankAccounts = bankAccountRepository.findAll();

        List<BankAccountEntity> updatedBankAccounts = bankAccounts.stream()
                .map(bankAccount -> {
                    BankAccountStatus newStatus = determineStatus(bankAccount, now);
                    if (newStatus != null && !newStatus.equals(bankAccount.getStatus())) {
                        bankAccount.setStatus(newStatus);
                        return bankAccount;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!updatedBankAccounts.isEmpty()) {
            bankAccountRepository.saveAll(updatedBankAccounts);
        }
    }

    @Override
    public BankAccountStatus determineStatus(BankAccountEntity bankAccount, LocalDate now) {
        LocalDate fromDate = toLocalDate(bankAccount.getFromDate());
        LocalDate toDate = toLocalDate(bankAccount.getToDate());
        if (toDate == null) {
            if (now.isBefore(fromDate)) {
                return BankAccountStatus.REGISTERED;
            } else {
                return BankAccountStatus.ACTIVE;
            }
        }
        if (toDate != null && now.isAfter(toDate)) {
            return BankAccountStatus.INACTIVE;
        } else if (fromDate != null && toDate != null && now.isBefore(fromDate)) {
            return BankAccountStatus.REGISTERED;
        } else if (fromDate != null && toDate != null && !now.isBefore(fromDate) && !now.isAfter(toDate)) {
            return BankAccountStatus.ACTIVE;
        }
        return null;

//        if (toDate == null && fromDate != null) {
//            // Nếu chỉ có fromDate không null
//            if (now.isBefore(fromDate)) {
//                return BankAccountStatus.REGISTERED;
//            } else {
//                return BankAccountStatus.ACTIVE;
//            }
//        }
//
//        if (fromDate != null && toDate != null) {
//            // Nếu cả fromDate và toDate đều không null
//            if (now.isBefore(fromDate)) {
//                return BankAccountStatus.REGISTERED;
//            } else if (now.isAfter(toDate)) {
//                return BankAccountStatus.INACTIVE;
//            } else {
//                return BankAccountStatus.ACTIVE;
//            }
//        }

    }

    private LocalDate toLocalDate(OffsetDateTime offsetDateTime) {
        return (offsetDateTime != null) ? offsetDateTime.toLocalDate() : null;
    }


}
