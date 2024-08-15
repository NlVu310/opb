package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.comon.PaginationRS;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.comon.SearchCriteria;
import com.openbanking.model.customer.CreateCustomer;
import com.openbanking.model.customer.Customer;
import com.openbanking.model.customer.UpdateCustomer;
import com.openbanking.model.partner.*;
import com.openbanking.model.permission.Permission;
import com.openbanking.model.permission.PermissionRS;
import com.openbanking.model.security.UserService;
import com.openbanking.model.system_configuration_auto_reconciliation.CreateSystemConfigurationAutoReconciliation;
import com.openbanking.model.system_configuration_auto_reconciliation.SystemConfigurationAutoReconciliation;
import com.openbanking.model.system_configuration_auto_reconciliation.UpdateSystemConfigurationAutoReconciliation;
import com.openbanking.service.PartnerService;
import com.openbanking.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/partner")
public class PartnerController extends BaseController<Partner, CreatePartner, UpdatePartner, Long> {
    @Autowired
    private PartnerService partnerService;

    @PostMapping("/list")
    public ResponseBuilder<PaginationRS<Partner>> getListPartner(@RequestBody(required = false) SearchPartnerRQ searchRQ) {
        var rs = partnerService.getListPartner(searchRQ);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @PostMapping("/create")
    public ResponseBuilder<Partner> create(@Valid @RequestBody CreatePartner rq , UserService userService) {
        partnerService.create(rq , userService.getCurrentUser().getId() );
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }
    @PutMapping("/update")
    public ResponseBuilder<Partner> update(@Valid @RequestBody UpdatePartner rq, UserService userService) {
        partnerService.update(rq, userService.getCurrentUser().getId());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }
    @GetMapping("/get")
    public ResponseBuilder<PartnerDetail> getDetailById(@RequestParam("id") Long id) {
        var rs = partnerService.getDetailById(id);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @DeleteMapping("/delete")
    public ResponseBuilder<Void> deleteByListId(@RequestParam List<Long> ids) {
        partnerService.deleteByListId(ids);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }
}