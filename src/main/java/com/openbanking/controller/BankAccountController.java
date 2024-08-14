package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.model.bank_account.BankAccount;
import com.openbanking.model.bank_account.CreateBankAccount;
import com.openbanking.model.bank_account.UpdateBankAccount;
import com.openbanking.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bank-account")
public class BankAccountController extends BaseController<BankAccount, CreateBankAccount, UpdateBankAccount, Long> {

    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("/get")
    public ResponseBuilder<?> getListBankAccountByCustomerId(@RequestParam("id") Long id) {
        var rs = bankAccountService.getListBankAccountByCustomerId(id);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
}
