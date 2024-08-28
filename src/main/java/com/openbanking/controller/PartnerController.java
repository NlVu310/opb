package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.comon.PaginationRS;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.comon.SearchCriteria;
import com.openbanking.model.partner.*;
import com.openbanking.model.security.UserService;
import com.openbanking.service.PartnerService;
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
    public ResponseBuilder<PaginationRS<Partner>> getListPartnerByAccount(@RequestParam Long accountId, @RequestBody(required = false) SearchPartnerRQ searchRQ) {
        var rs = partnerService.getListPartnerByAccount(accountId, searchRQ);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @PostMapping("/get-all")
    public ResponseBuilder<PaginationRS<Partner>> getAll(@RequestBody(required = false) SearchCriteria rq) {
        PaginationRS<Partner> rsLst = partnerService.getAll(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rsLst);
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