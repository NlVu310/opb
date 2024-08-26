package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.comon.PaginationRS;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.comon.SearchCriteria;
import com.openbanking.model.customer.*;
import com.openbanking.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController extends BaseController<Customer, CreateCustomer, UpdateCustomer, Long> {

    @Autowired
    private CustomerService customerService;


    @PostMapping("/list")
    public ResponseBuilder<PaginationRS<Customer>> getListCustomer(@RequestBody(required = false) SearchCustomerRQ searchRQ) {
        var rs = customerService.getListCustomer(searchRQ);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @PostMapping("/get-all")
    public ResponseBuilder<PaginationRS<Customer>> getAll(@RequestBody(required = false) SearchCriteria rq) {
        PaginationRS<Customer> rsLst = customerService.getAll(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rsLst);
    }

    @PostMapping("/create")
    public ResponseBuilder<?> create(@Valid @RequestBody CreateCustomer rq) {
        customerService.create(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }

    @PutMapping("/update")
    public ResponseBuilder<?> update(@Valid @RequestBody UpdateCustomer rq) {
        customerService.update(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }

    @GetMapping("/get-by-id")
    public ResponseBuilder<CustomerDetail> getByCustomerDetail(@RequestParam("id") Long id) {
        var rs = customerService.getCustomerDetail(id);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping("/get-customer-transaction")
    public ResponseBuilder<CustomerTransactionDetail> getByCustomerTransactionDetail(@RequestParam("id") Long id) {
        var rs = customerService.getCustomerTransactionDetail(id);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @DeleteMapping("/delete")
    public ResponseBuilder<Void> deleteByListId(@RequestParam("ids") List<Long> ids) {
        customerService.deleteByListId(ids);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }
}