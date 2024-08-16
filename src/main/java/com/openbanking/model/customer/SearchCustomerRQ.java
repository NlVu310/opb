package com.openbanking.model.customer;

import com.openbanking.comon.SearchCriteria;
import lombok.Data;

@Data
public class SearchCustomerRQ extends SearchCriteria {
    private Long id;
    private String name;
    private String taxNo;
    private String address;
    private String status;
}
