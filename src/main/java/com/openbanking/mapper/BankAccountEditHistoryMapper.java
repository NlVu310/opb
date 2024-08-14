package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.BankAccountEditHistoryEntity;
import com.openbanking.model.bank_account_edit_history.BankAccountEditHistory;
import com.openbanking.model.bank_account_edit_history.CreateBankAccountEditHistory;
import com.openbanking.model.bank_account_edit_history.UpdateBankAccountEditHistory;
import org.mapstruct.Mapper;

@Mapper
public interface BankAccountEditHistoryMapper extends BaseMapper<BankAccountEditHistoryEntity, BankAccountEditHistory, CreateBankAccountEditHistory, UpdateBankAccountEditHistory> {
}
