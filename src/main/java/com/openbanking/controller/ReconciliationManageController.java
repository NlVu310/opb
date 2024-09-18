package com.openbanking.controller;

import com.openbanking.comon.PaginationRS;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.model.reconciliation_manage.ReconciliationIconnect;
import com.openbanking.model.reconciliation_manage.ReconciliationManage;
import com.openbanking.model.reconciliation_manage.ReconciliationManageDetail;
import com.openbanking.model.reconciliation_manage.SearchReconciliationRQ;
import com.openbanking.service.ReconciliationManageService;
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

    @PostMapping("/list")
    public ResponseBuilder<PaginationRS<ReconciliationManage>> getAll(@RequestBody(required = false) SearchReconciliationRQ searchRQ) {
        var rs = reconciliationManageService.getListReconciliation(searchRQ);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping("/get")
    public ResponseBuilder<ReconciliationManageDetail> getDetailById(@RequestParam("id") Long id) {
        var rs = reconciliationManageService.getDetailById(id);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @PostMapping("/gw-iconnect")
    public void receiveReconciliationTransactionFromIconnect(@RequestBody List<ReconciliationIconnect> rq) {
        reconciliationManageService.handleIconnectReconciliations(rq);
    }
}
