package com.openbanking.model.customer;

import com.openbanking.comon.SearchCriteria;
import com.openbanking.enums.CustomerStatus;
import lombok.Data;

import javax.persistence.Enumerated;

@Data
public class SearchCustomerRQ extends SearchCriteria {
    private Long id;
    private String name;
    private String taxNo;
    private String address;
    private String code;
    private CustomerStatus status;
    private Boolean isParent;
}
