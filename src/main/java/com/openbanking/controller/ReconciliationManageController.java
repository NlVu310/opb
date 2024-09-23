package com.openbanking.controller;

import com.openbanking.comon.PaginationRS;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.model.reconciliation_manage.*;
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

    @PostMapping("/gw-iconnect")
    public void receiveReconciliationFromIconnect(@RequestBody List<ReconciliationIconnect> rq) {
        log.info("Received transaction from Iconnect : {}", rq);
        reconciliationManageService.handleIconnectReconciliations(rq);
    }
    @PostMapping("/perform")
    public void handlePerformReconciliation(@RequestBody ReconciliationManageRequest rq , @RequestParam Long accountId ) {
        reconciliationManageService.performReconciliation(rq , accountId);
    }
}
