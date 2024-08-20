package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.comon.PaginationRS;
import com.openbanking.model.partner.PartnerDetail;
import com.openbanking.model.transaction_manage.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface TransactionManageService extends BaseService<TransactionManage, CreateTransactionManage, UpdateTransactionManage, Long> {
    PaginationRS<TransactionManage> getListTransaction(SearchTransactionManageRQ searchRQ);

    ResponseEntity<InputStreamResource> exportTransactionToExcel() throws IOException;
    TransactionManageDetail getDetailById(Long id);
}
