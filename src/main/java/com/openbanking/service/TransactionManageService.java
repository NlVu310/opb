package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.comon.PaginationRS;
import com.openbanking.model.transaction_manage.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionManageService extends BaseService<TransactionManage, CreateTransactionManage, UpdateTransactionManage, Long> {
    PaginationRS<TransactionManage> getListTransaction(SearchTransactionManageRQ searchRQ);

    TransactionManageDetail getDetailById(Long id);
    void handleIconnectTransactions(List<Iconnect> iconnects);
}
