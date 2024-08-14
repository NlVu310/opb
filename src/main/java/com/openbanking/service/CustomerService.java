package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.model.customer.CreateCustomer;
import com.openbanking.model.customer.Customer;
import com.openbanking.model.customer.UpdateCustomer;
import com.openbanking.model.system_configuration_auto_reconciliation.CreateSystemConfigurationAutoReconciliation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService  extends BaseService<Customer, CreateCustomer, UpdateCustomer, Long> {
    void create(CreateCustomer createCustomer);

    List<Customer> getListCustomerTypeByAccountId(Long id);
}
