package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.comon.PaginationRS;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.comon.SearchCriteria;
import com.openbanking.model.security.UserService;
import com.openbanking.model.system_configuration_transaction_content.CreateSystemConfigurationTransactionContent;
import com.openbanking.model.system_configuration_transaction_content.SystemConfigurationTransactionContent;
import com.openbanking.model.system_configuration_transaction_content.UpdateSystemConfigurationTransactionContent;
import com.openbanking.service.SystemConfigurationTransactionContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class SystemConfigurationTransactionContentController extends BaseController<SystemConfigurationTransactionContent, CreateSystemConfigurationTransactionContent, UpdateSystemConfigurationTransactionContent, Long> {
    @Autowired
    private SystemConfigurationTransactionContentService systemConfigurationTransactionContentService;

    @PostMapping("/list")
    public ResponseBuilder<PaginationRS<SystemConfigurationTransactionContent>> getListTransaction(@RequestBody SearchCriteria searchCriteria) {
        var rs = systemConfigurationTransactionContentService.getAll(searchCriteria);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }


    @PostMapping("/create")
    public ResponseBuilder<SystemConfigurationTransactionContent> create(@RequestBody CreateSystemConfigurationTransactionContent rq , UserService userService) {
        systemConfigurationTransactionContentService.create(rq , userService.getCurrentUser().getId());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }

    @PutMapping("/update")
    public ResponseBuilder<SystemConfigurationTransactionContent> update(@RequestBody UpdateSystemConfigurationTransactionContent rq, UserService userService) {
        systemConfigurationTransactionContentService.update(rq, userService.getCurrentUser().getId());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }

    @DeleteMapping
    public ResponseBuilder<Void> deleteByListId(@RequestParam List<Long> ids) {
        systemConfigurationTransactionContentService.deleteListById(ids);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }

    @GetMapping("/get")
    public ResponseBuilder<SystemConfigurationTransactionContent> getById(@RequestParam("id") Long id) {
        var rs = systemConfigurationTransactionContentService.getById(id);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

}