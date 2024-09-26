package com.openbanking.controller;

import com.openbanking.comon.PaginationRS;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.model.account.Account;
import com.openbanking.model.reconciliation_manage.*;
import com.openbanking.model.transaction_manage.DebtClearance;
import com.openbanking.model.transaction_manage.SearchTransactionManageRQ;
import com.openbanking.model.transaction_manage.TransactionManage;
//import com.openbanking.service.FeignService;
import com.openbanking.service.FeignService;
import com.openbanking.service.ReconciliationManageService;
import com.openbanking.service.TransactionManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reconciliation-manage")
@Slf4j
public class ReconciliationManageController {
    @Autowired
    private ReconciliationManageService reconciliationManageService;
    @Autowired
    private TransactionManageService transactionManageService;
    @Autowired
    private FeignService feignService;

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
    public ResponseBuilder<Void> handlePerformReconciliation(@RequestBody ReconciliationManageRequest rq, @RequestParam Long accountId) {
        reconciliationManageService.performReconciliation(rq, accountId);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }

//    @PostMapping("/clearance-reconciliation")
//    public ResponseBuilder<Void> handleDebtClearanceReconciliation(@RequestBody List<DebtClearance> debtClearances) {
//        feignService.handleClearanceReconciliation(debtClearances);
//        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
//    }

    @PostMapping("/test")
    public ResponseBuilder<FeignResult> handleDebtClearanceReconciliation1(@RequestBody TestFeign feign) {
        String authorizationHeader = "Basic YXBlYzphcGVjMTIz"; // Giá trị cứng
        String requestId = "20220510155610517-7babdf6a-691c-407d-9b5f-fd0f6320037f"; // Giá trị cứng
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", authorizationHeader);
        headers.put("REQUESTID", requestId);

        FeignResult response = feignService.handleClearanceReconciliation1(feign, headers);

        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", response);
    }
}