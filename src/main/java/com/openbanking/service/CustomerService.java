package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.model.account_type.AccountTypeDetail;
import com.openbanking.model.account_type.UpdateAccountType;
import com.openbanking.model.customer.CreateCustomer;
import com.openbanking.model.customer.Customer;
import com.openbanking.model.customer.CustomerDetail;
import com.openbanking.model.customer.UpdateCustomer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService  extends BaseService<Customer, CreateCustomer, UpdateCustomer, Long> {
    void create(CreateCustomer createCustomer);
    void update(UpdateCustomer updateCustomer);
    CustomerDetail getCustomerDetail(Long id);

    List<Customer> getListCustomerTypeByAccountId(Long id);
    void deleteByListId(List<Long> ids);
}
