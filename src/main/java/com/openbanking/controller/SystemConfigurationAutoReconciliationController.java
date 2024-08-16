package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.comon.PaginationRS;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.comon.SearchCriteria;
import com.openbanking.model.security.UserService;
import com.openbanking.model.system_configuration_auto_reconciliation.CreateSystemConfigurationAutoReconciliation;
import com.openbanking.model.system_configuration_auto_reconciliation.SystemConfigurationAutoReconciliation;
import com.openbanking.model.system_configuration_auto_reconciliation.UpdateSystemConfigurationAutoReconciliation;
import com.openbanking.service.SystemConfigurationAutoReconciliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reconciliation")
public class SystemConfigurationAutoReconciliationController extends BaseController<SystemConfigurationAutoReconciliation, CreateSystemConfigurationAutoReconciliation, UpdateSystemConfigurationAutoReconciliation, Long> {
    @Autowired
    private SystemConfigurationAutoReconciliationService systemConfigurationAutoReconciliationService;

    @PostMapping("/create")
    public ResponseBuilder<?> create(@Valid @RequestBody CreateSystemConfigurationAutoReconciliation rq) {
        systemConfigurationAutoReconciliationService.create(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }
    @PutMapping("/update")
    public ResponseBuilder<SystemConfigurationAutoReconciliation> update(@Valid @RequestBody UpdateSystemConfigurationAutoReconciliation rq, UserService userService) {
        systemConfigurationAutoReconciliationService.update(rq, userService.getCurrentUser().getId());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }
    @GetMapping("/get")
    public ResponseBuilder<SystemConfigurationAutoReconciliation> getById(@RequestParam("id") Long id) {
        var rs = systemConfigurationAutoReconciliationService.getDetailById(id);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @DeleteMapping("/delete")
    public ResponseBuilder<Void> deleteByListId(@RequestParam List<Long> ids) {
        systemConfigurationAutoReconciliationService.deleteListById(ids);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }

    @PostMapping("/list")
    public ResponseBuilder<PaginationRS<SystemConfigurationAutoReconciliation>> getAll(@RequestBody(required = false) SearchCriteria rq) {
        PaginationRS<SystemConfigurationAutoReconciliation> rsLst = systemConfigurationAutoReconciliationService.getAll(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rsLst);
    }

}
