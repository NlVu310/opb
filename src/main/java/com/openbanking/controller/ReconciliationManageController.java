package com.openbanking.controller;

import com.openbanking.comon.PaginationRS;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.model.reconciliation_manage.ReconciliationIconnect;
import com.openbanking.model.reconciliation_manage.ReconciliationManage;
import com.openbanking.model.reconciliation_manage.ReconciliationManageDetail;
import com.openbanking.model.reconciliation_manage.SearchReconciliationRQ;
import com.openbanking.model.transaction_manage.SearchTransactionManageRQ;
import com.openbanking.model.transaction_manage.TransactionManage;
import com.openbanking.service.ReconciliationManageService;
import com.openbanking.service.TransactionManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reconciliation-manage")
@Slf4j
public class ReconciliationManageController {
    @Autowired
    private ReconciliationManageService reconciliationManageService;

    @Autowired
    private TransactionManageService transactionManageService;

    @PostMapping("/list")
    public ResponseBuilder<PaginationRS<TransactionManage>> getAll(@RequestBody(required = false) SearchTransactionManageRQ searchRQ) {
        var rs = transactionManageService.getListTransactionRecon(searchRQ);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

}
