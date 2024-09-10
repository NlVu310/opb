package com.openbanking.controller;

import com.openbanking.comon.ResponseBuilder;
import com.openbanking.model.reconciliation_manage.ReconciliationIconnect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reconciliation-manage")
@Slf4j
public class ReconciliationManageController {
    @PostMapping("/gw-iconnect")
    public void receiveReconciliationTransactionFromIconnect(@RequestBody List<ReconciliationIconnect> rq) {
        log.info("Received transaction from Iconnect : {}", rq);
    }
}
