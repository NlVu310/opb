package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.BankAccountEditHistoryEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountEditHistoryRepository extends BaseRepository<BankAccountEditHistoryEntity, Long> {
}
