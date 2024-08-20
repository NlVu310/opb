package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.PartnerEntity;
import com.openbanking.entity.SystemConfigurationSourceEntity;
import com.openbanking.entity.TransactionManageReconciliationHistoryEntity;
import com.openbanking.model.transaction_manage_reconciliation_history.TransactionManageReconciliationHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionManageReconciliationHistoryRepository extends BaseRepository<TransactionManageReconciliationHistoryEntity, Long> {
    @Query(value = "select t from TransactionManageReconciliationHistoryEntity t join TransactionManageEntity tm on t.transactionManageId = tm.id and t.transactionManageId = :id")
    List<TransactionManageReconciliationHistoryEntity> getListByTransactionManageId(Long id);

}
