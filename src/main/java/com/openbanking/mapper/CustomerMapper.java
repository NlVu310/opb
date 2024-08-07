package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.CustomerEntity;
import com.openbanking.model.customer.CreateCustomer;
import com.openbanking.model.customer.Customer;
import com.openbanking.model.customer.UpdateCustomer;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper extends BaseMapper<CustomerEntity, Customer, CreateCustomer, UpdateCustomer> {
}

