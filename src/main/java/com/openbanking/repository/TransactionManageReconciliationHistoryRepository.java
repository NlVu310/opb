package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.TransactionManageReconciliationHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionManageReconciliationHistoryRepository extends BaseRepository<TransactionManageReconciliationHistoryEntity, Long> {
    List<TransactionManageReconciliationHistoryEntity> findByTransactionIdAndReconciliationSource(String transactionId, String reconciliationSource);
    List<TransactionManageReconciliationHistoryEntity> findByTransactionManageId(Long transactionManageId);
    @Query("SELECT t FROM TransactionManageReconciliationHistoryEntity t " +
            "WHERE t.id IN (SELECT t2.id FROM TransactionManageReconciliationHistoryEntity t2 " +
            "WHERE t2.createdAt = (SELECT MAX(t3.createdAt) FROM TransactionManageReconciliationHistoryEntity t3 " +
            "WHERE t3.transactionManageId = t2.transactionManageId))")
    List<TransactionManageReconciliationHistoryEntity> findLatestForEachTransactionManageId();

    @Query("SELECT t FROM TransactionManageReconciliationHistoryEntity t " +
            "WHERE t.transactionManageId = :transactionManageId " +
            "AND t.createdAt = (SELECT MAX(t2.createdAt) FROM TransactionManageReconciliationHistoryEntity t2 " +
            "WHERE t2.transactionId = t.transactionId " +
            "AND t2.reconciliationSource = t.reconciliationSource " +
            "AND t2.transactionManageId = t.transactionManageId " +
            "AND t2.createdBy = t.createdBy) ")
    List<TransactionManageReconciliationHistoryEntity> findLatestByTransactionManageIdAndCreatedBy(
            @Param("transactionManageId") Long transactionManageId);

}
