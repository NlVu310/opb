package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.comon.SearchCriteria;
import com.openbanking.model.Account;
import com.openbanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController extends BaseController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/getAll")
    public ResponseBuilder<List<Account>> getAll(@RequestBody(required = false) SearchCriteria rq) {
        List<Account> listAccount = accountService.getAll(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", listAccount);
    }
}
