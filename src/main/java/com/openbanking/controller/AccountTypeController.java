package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.comon.PaginationRS;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.comon.SearchCriteria;
import com.openbanking.model.account_type.*;
import com.openbanking.service.AccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/account-type")
public class AccountTypeController extends BaseController<AccountType, CreateAccountType, UpdateAccountType, Long> {

    @Autowired
    private AccountTypeService accountTypeService;


    @GetMapping("/list")
    public ResponseBuilder<PaginationRS<AccountTypeInfo>> getListAccountTypeById(@RequestParam("id") Long id, SearchCriteria searchCriteria) {
        var rs = accountTypeService.getListAccountTypeByAccountId(id, searchCriteria);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping("/get")
    public ResponseBuilder<AccountTypeDetail> getDetailById(@RequestParam("id") Long id) {
        var rs = accountTypeService.getAccountTypeDetail(id);
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
