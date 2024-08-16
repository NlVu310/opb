package com.openbanking.model.partner;

import com.openbanking.comon.SearchCriteria;
import lombok.Data;

@Data
public class SearchPartnerRQ extends SearchCriteria {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String status;
}
