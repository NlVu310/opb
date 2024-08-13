package com.openbanking.model.account_type;

import com.openbanking.comon.SearchCriteria;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Date;

@Data
public class SearchAccountTypeRQ extends SearchCriteria {
    private OffsetDateTime date;
}

