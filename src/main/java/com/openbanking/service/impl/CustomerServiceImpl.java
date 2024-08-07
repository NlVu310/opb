package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.AccountTypeEntity;
import com.openbanking.entity.CustomerEntity;
import com.openbanking.mapper.AccountTypeMapper;
import com.openbanking.mapper.CustomerMapper;
import com.openbanking.model.account_type.AccountType;
import com.openbanking.model.account_type.CreateAccountType;
import com.openbanking.model.account_type.UpdateAccountType;
import com.openbanking.model.customer.CreateCustomer;
import com.openbanking.model.customer.Customer;
import com.openbanking.model.customer.UpdateCustomer;
import com.openbanking.repository.AccountTypeRepository;
import com.openbanking.repository.CustomerRepository;
import com.openbanking.service.AccountTypeService;
import com.openbanking.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl  extends BaseServiceImpl<CustomerEntity, Customer, CreateCustomer, UpdateCustomer, Long> implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerMapper customerMapper;

    public CustomerServiceImpl(BaseRepository<CustomerEntity, Long> repository, BaseMapper<CustomerEntity, Customer, CreateCustomer, UpdateCustomer> mapper) {
        super(repository, mapper);
    }


    @Override
    public List<Customer> getListCustomerById(Long id) {
        List<CustomerEntity> customerEntities = customerRepository.getListCustomerTypeByAccountId(id);
        var rs = customerEntities.stream().map(customerMapper::toDTO).collect(Collectors.toList());
        return rs;
    }
}