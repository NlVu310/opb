package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.BankAccountEditHistoryEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountEditHistoryRepository extends BaseRepository<BankAccountEditHistoryEntity, Long> {
    void deleteByBankAccountIdIn(List<Long> bankAccountIds);
}
