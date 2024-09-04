package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.comon.PaginationRS;
import com.openbanking.model.customer.*;
import com.openbanking.model.transaction_manage.SearchTransactionManageRQ;
import com.openbanking.model.transaction_manage.TransactionManage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService  extends BaseService<Customer, CreateCustomer, UpdateCustomer, Long> {
    void create(CreateCustomer createCustomer);
    void update(UpdateCustomer updateCustomer);
    CustomerDetail getCustomerDetail(Long id);
    PaginationRS<TransactionManage> getCustomerTransactionDetail(SearchTransactionManageRQ SearchRQ , Long id);
    void deleteByListId(List<Long> ids);
    PaginationRS<Customer> getListCustomerByAccount(Long accountId, SearchCustomerRQ searchRQ);

    List<Customer> getListParentCustomers();
}
