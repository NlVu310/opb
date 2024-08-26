package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.comon.PaginationRS;
import com.openbanking.model.account_type.AccountTypeDetail;
import com.openbanking.model.account_type.UpdateAccountType;
import com.openbanking.model.customer.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService  extends BaseService<Customer, CreateCustomer, UpdateCustomer, Long> {
    void create(CreateCustomer createCustomer);
    void update(UpdateCustomer updateCustomer);
    CustomerDetail getCustomerDetail(Long id);
    CustomerTransactionDetail getCustomerTransactionDetail(Long id);
    void deleteByListId(List<Long> ids);
    PaginationRS<Customer> getListCustomer(SearchCustomerRQ searchRQ);
}
