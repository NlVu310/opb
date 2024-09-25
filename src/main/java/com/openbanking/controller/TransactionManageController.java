package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.comon.PaginationRS;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.model.transaction_manage.*;
import com.openbanking.service.TransactionManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction-manage")
@Slf4j
public class TransactionManageController extends BaseController<TransactionManage, CreateTransactionManage, UpdateTransactionManage, Long> {
    @Autowired
    private TransactionManageService transactionManageService;

    @PostMapping("/list")
    public ResponseBuilder<PaginationRS<TransactionManage>> getAll(@RequestBody(required = false) SearchTransactionManageRQ searchRQ) {
        var rs = transactionManageService.getListTransaction(searchRQ);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping("/get")
    public ResponseBuilder<TransactionManageDetail> getDetailById(@RequestParam("id") Long id) {
        var rs = transactionManageService.getDetailById(id);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @PostMapping("/gw-iconnect")
    public void receiveTransactionFromIconnect(@RequestBody List<Iconnect> rq) {
        log.info("Received transaction from Iconnect : {}", rq);
        transactionManageService.handleIconnectTransactions(rq);
    }

    @PostMapping("/gw-debt-clearance")
    public void receiveTransactionFromDebtClearance(@RequestBody DebtClearance rq) {
        transactionManageService.handleDebtClearanceTransaction(rq);
    }

}
