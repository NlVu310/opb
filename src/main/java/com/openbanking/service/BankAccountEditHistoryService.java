package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.entity.BankAccountEditHistoryEntity;
import com.openbanking.model.bank_account_edit_history.BankAccountEditHistory;
import com.openbanking.model.bank_account_edit_history.CreateBankAccountEditHistory;
import com.openbanking.model.bank_account_edit_history.UpdateBankAccountEditHistory;
import org.springframework.stereotype.Service;

@Service
public interface BankAccountEditHistoryService extends BaseService<BankAccountEditHistory, CreateBankAccountEditHistory, UpdateBankAccountEditHistory, Long> {
}
