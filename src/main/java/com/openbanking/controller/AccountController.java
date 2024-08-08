package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.comon.SearchCriteria;
import com.openbanking.model.account.Account;
import com.openbanking.model.account.CreateAccount;
import com.openbanking.model.account.SearchAccountRQ;
import com.openbanking.model.account.UpdateAccount;
import com.openbanking.model.account_type.AccountType;
import com.openbanking.model.security.UserService;
import com.openbanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController extends BaseController <Account, CreateAccount, UpdateAccount, Long>{

    @Autowired
    private AccountService accountService;

    @PostMapping("/getAll")
    public ResponseBuilder<List<Account>> getAll(@RequestBody(required = false) SearchAccountRQ rq) {
        List<Account> listAccount = accountService.getAll(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", listAccount);
    }

    @PostMapping("/create")
    public ResponseBuilder<Account> create(@RequestBody CreateAccount rq, UserService userService) {
        var rs = accountService.create(rq, userService.getCurrentUser().getId());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping("/reset-password")
    public ResponseBuilder<Account> resetPassword(@RequestParam Long id) {
        accountService.resetPassword(id);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }

    @PutMapping("/update")
    public ResponseBuilder<Account> update(@RequestBody UpdateAccount rq, UserService userService) {
        var rs = accountService.update(rq, userService.getCurrentUser().getId());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @DeleteMapping("/delete")
    public ResponseBuilder<Account> delete(@RequestParam List<Long> ids) {
        accountService.deleteByListId(ids);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }

}
