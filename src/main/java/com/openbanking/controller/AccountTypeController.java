package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.model.account_type.AccountType;
import com.openbanking.model.account_type.CreateAccountType;
import com.openbanking.model.account_type.UpdateAccountType;
import com.openbanking.service.AccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account-type")
public class AccountTypeController {

    @Autowired
    private AccountTypeService accountTypeService;


    @GetMapping("/list")
    public ResponseBuilder<List<AccountType>> getListAccountTypeById(@RequestParam("id") Long id) {
        var rs = accountTypeService.getListAccountTypeById(id);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @PostMapping("/create")
    public ResponseBuilder<?> create(@RequestBody CreateAccountType rq) {
        accountTypeService.create(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }

    @PutMapping("/update")
    public ResponseBuilder<?> update(@RequestBody UpdateAccountType rq) {
        accountTypeService.update(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }

    @DeleteMapping("/delete")
    public ResponseBuilder<?> deleteByAccountTypeId(@RequestParam("id") Long id) {
        accountTypeService.deleteById(id);
        return new ResponseBuilder<>(HttpStatus.NO_CONTENT.value(), "Success", null);
    }

}
