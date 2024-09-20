package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.AwaitingReconciliationTransactionEntity;
import com.openbanking.entity.TransactionManageEntity;
import com.openbanking.model.awaiting_reconciliation_transactions.AwaitingReconciliationTransaction;
import com.openbanking.model.awaiting_reconciliation_transactions.CreateAwaitingReconciliationTransaction;
import com.openbanking.model.awaiting_reconciliation_transactions.UpdateAwaitingReconciliationTransaction;
import com.openbanking.model.transaction_manage.CreateTransactionManage;
import com.openbanking.model.transaction_manage.TransactionManage;
import com.openbanking.model.transaction_manage.UpdateTransactionManage;
import com.openbanking.service.AwaitingReconciliationTransactionService;
import com.openbanking.service.TransactionManageService;
import org.springframework.stereotype.Service;

@Service
public class AwaitingReconciliationTransactionServiceImpl extends BaseServiceImpl<AwaitingReconciliationTransactionEntity, AwaitingReconciliationTransaction, CreateAwaitingReconciliationTransaction, UpdateAwaitingReconciliationTransaction, Long> implements AwaitingReconciliationTransactionService {
    public AwaitingReconciliationTransactionServiceImpl(BaseRepository<AwaitingReconciliationTransactionEntity, Long> repository, BaseMapper<AwaitingReconciliationTransactionEntity, AwaitingReconciliationTransaction, CreateAwaitingReconciliationTransaction, UpdateAwaitingReconciliationTransaction> mapper) {
        super(repository, mapper);
    }
}
