package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.comon.PaginationRS;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.comon.SearchCriteria;
import com.openbanking.model.partner.PartnerDetail;
import com.openbanking.model.system_configuration_source.SystemConfigurationSource;
import com.openbanking.model.transaction_manage.*;
import com.openbanking.service.TransactionManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/transaction-manage")
public class TransactionManageController extends BaseController<TransactionManage, CreateTransactionManage, UpdateTransactionManage, Long> {
    @Autowired
    private TransactionManageService transactionManageService;

    @PostMapping("/list")
    public ResponseBuilder<PaginationRS<TransactionManage>> getAll(@RequestBody(required = false) SearchTransactionManageRQ searchRQ) {
        var rs = transactionManageService.getListTransaction(searchRQ);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping("/excel")
    public ResponseEntity<?> exportTransactionToExcel() throws IOException {
        return transactionManageService.exportTransactionToExcel();
    }

    @GetMapping("/get")
    public ResponseBuilder<TransactionManageDetail> getDetailById(@RequestParam("id") Long id) {
        var rs = transactionManageService.getDetailById(id);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
}
