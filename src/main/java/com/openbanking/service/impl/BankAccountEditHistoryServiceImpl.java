package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.BankAccountEditHistoryEntity;
import com.openbanking.mapper.BankAccountEditHistoryMapper;
import com.openbanking.model.bank_account_edit_history.BankAccountEditHistory;
import com.openbanking.model.bank_account_edit_history.CreateBankAccountEditHistory;
import com.openbanking.model.bank_account_edit_history.UpdateBankAccountEditHistory;
import com.openbanking.repository.BankAccountEditHistoryRepository;
import com.openbanking.service.BankAccountEditHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountEditHistoryServiceImpl extends BaseServiceImpl<BankAccountEditHistoryEntity, BankAccountEditHistory, CreateBankAccountEditHistory, UpdateBankAccountEditHistory, Long> implements BankAccountEditHistoryService {

    @Autowired
    private BankAccountEditHistoryRepository bankAccountEditHistoryRepository;
    @Autowired
    private BankAccountEditHistoryMapper bankAccountEditHistoryMapper;

    public BankAccountEditHistoryServiceImpl(BaseRepository<BankAccountEditHistoryEntity, Long> repository, BaseMapper<BankAccountEditHistoryEntity, BankAccountEditHistory, CreateBankAccountEditHistory, UpdateBankAccountEditHistory> mapper) {
        super(repository, mapper);
    }

    @Override
    public List<BankAccountEditHistory> getListBankAccountEditHistoryByBankAccountId(Long id) {
            List<BankAccountEditHistoryEntity> bankAccountEntities  = bankAccountEditHistoryRepository.getListBankAccountEditHistoryByBankAccountId(id);
            return bankAccountEditHistoryMapper.toDTOs(bankAccountEntities);
    }
}
