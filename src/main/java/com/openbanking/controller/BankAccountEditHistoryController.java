package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.model.bank_account_edit_history.BankAccountEditHistory;
import com.openbanking.model.bank_account_edit_history.CreateBankAccountEditHistory;
import com.openbanking.model.bank_account_edit_history.UpdateBankAccountEditHistory;
import com.openbanking.model.system_configuration_auto_reconciliation.SystemConfigurationAutoReconciliation;
import com.openbanking.service.BankAccountEditHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bank-account/edit-history")
public class BankAccountEditHistoryController extends BaseController<BankAccountEditHistory, CreateBankAccountEditHistory, UpdateBankAccountEditHistory, Long> {

    @Autowired
    BankAccountEditHistoryService bankAccountEditHistoryService;
    @GetMapping("/get")
    public ResponseBuilder<BankAccountEditHistory> getById(@RequestParam("id") Long id) {
        var rs = bankAccountEditHistoryService.getById(id);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
}
