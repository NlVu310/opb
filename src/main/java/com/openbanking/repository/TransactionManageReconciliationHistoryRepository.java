package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.TransactionManageReconciliationHistoryEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionManageReconciliationHistoryRepository extends BaseRepository<TransactionManageReconciliationHistoryEntity, Long> {
    List<TransactionManageReconciliationHistoryEntity> findByTransactionIdAndReconciliationSource(String transactionId, String reconciliationSource);
}
