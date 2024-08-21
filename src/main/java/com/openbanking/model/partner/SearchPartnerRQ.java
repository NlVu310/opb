package com.openbanking.model.partner;

import com.openbanking.comon.SearchCriteria;
import com.openbanking.enums.PartnerStatus;
import lombok.Data;

@Data
public class SearchPartnerRQ extends SearchCriteria {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String code;

    private PartnerStatus status;
}
