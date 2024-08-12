package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.comon.PaginationRS;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.comon.SearchCriteria;
import com.openbanking.model.customer.CreateCustomer;
import com.openbanking.model.customer.Customer;
import com.openbanking.model.customer.UpdateCustomer;
import com.openbanking.model.partner.CreatePartner;
import com.openbanking.model.partner.Partner;
import com.openbanking.model.partner.UpdatePartner;
import com.openbanking.model.permission.Permission;
import com.openbanking.model.permission.PermissionRS;
import com.openbanking.service.PartnerService;
import com.openbanking.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partner")
public class PartnerController extends BaseController<Partner, CreatePartner, UpdatePartner, Long> {
    @Autowired
    private PartnerService partnerService;

    @GetMapping("/list")
    public ResponseBuilder<PaginationRS<Partner>> getListPartner(@RequestBody(required = false) SearchCriteria searchCriteria) {
        var rs = partnerService.getAll(searchCriteria);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
}