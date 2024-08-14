package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.BankAccountEditHistoryEntity;
import com.openbanking.entity.BankAccountEntity;
import com.openbanking.model.bank_account.BankAccount;
import com.openbanking.model.bank_account.CreateBankAccount;
import com.openbanking.model.bank_account.UpdateBankAccount;
import com.openbanking.model.bank_account_edit_history.BankAccountEditHistory;
import com.openbanking.model.bank_account_edit_history.CreateBankAccountEditHistory;
import com.openbanking.model.bank_account_edit_history.UpdateBankAccountEditHistory;
import com.openbanking.repository.BankAccountEditHistoryRepository;
import com.openbanking.service.BankAccountEditHistoryService;
import com.openbanking.service.BankAccountService;
import org.springframework.stereotype.Service;

@Service
public class BankAccountEditHistoryServiceImpl extends BaseServiceImpl<BankAccountEditHistoryEntity, BankAccountEditHistory, CreateBankAccountEditHistory, UpdateBankAccountEditHistory, Long> implements BankAccountEditHistoryService {

    public BankAccountEditHistoryServiceImpl(BaseRepository<BankAccountEditHistoryEntity, Long> repository, BaseMapper<BankAccountEditHistoryEntity, BankAccountEditHistory, CreateBankAccountEditHistory, UpdateBankAccountEditHistory> mapper) {
        super(repository, mapper);
    }
}
