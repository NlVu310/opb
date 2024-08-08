package com.openbanking.comon;


import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class SearchCriteria {
    private String term;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;
}