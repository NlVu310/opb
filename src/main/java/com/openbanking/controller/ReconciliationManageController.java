package com.openbanking.controller;

import com.openbanking.comon.ResponseBuilder;
import com.openbanking.model.reconciliation_manage.ReconciliationIconnect;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reconciliation-manage")
public class ReconciliationManageController {
    @PostMapping("/gw-iconnect")
    public ResponseBuilder<Void> receiveReconciliationTransactionFromIconnect(@RequestBody List<ReconciliationIconnect> rq) {
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }
}
