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
public class AccountTypeController extends BaseController<AccountType, CreateAccountType, UpdateAccountType, Long> {

    @Autowired
    private AccountTypeService accountTypeService;


    @GetMapping("/list")
    public ResponseBuilder<List<AccountType>> getListAccountTypeById(@RequestParam("id") Long id) {
        var rs = accountTypeService.getListAccountTypeById(id);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }


}
