package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.model.customer.CreateCustomer;
import com.openbanking.model.customer.Customer;
import com.openbanking.model.customer.UpdateCustomer;
import com.openbanking.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController extends BaseController<Customer, CreateCustomer, UpdateCustomer, Long> {

    @Autowired
    private CustomerService customerService;


    @GetMapping("/list")
    public ResponseBuilder<List<Customer>> getListAccountTypeById(@RequestParam("id") Long id) {
        var rs = customerService.getListCustomerById(id);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
}