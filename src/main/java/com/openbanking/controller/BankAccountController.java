package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.model.bank_account.BankAccount;
import com.openbanking.model.bank_account.CreateBankAccount;
import com.openbanking.model.bank_account.SearchBankAccountRQ;
import com.openbanking.model.bank_account.UpdateBankAccount;
import com.openbanking.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank-account")
public class BankAccountController extends BaseController<BankAccount, CreateBankAccount, UpdateBankAccount, Long> {

    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("/by-customer")
    public ResponseBuilder<?> getListBankAccountByCustomerId(@RequestBody(required = false) SearchBankAccountRQ searchRQ) {
        var rs = bankAccountService.getListBankAccountByCustomerId(searchRQ);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping("/list-status")
    public ResponseBuilder<?> getListStatus() {
        var rs = bankAccountService.findDistinctStatus();
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping("/list-partner/by-customer")
    public ResponseBuilder<?> getListPartner(@RequestParam Long customerId) {
        var rs = bankAccountService.getDistinctPartnerInfoByCustomer(customerId);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
}
