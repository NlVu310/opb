package com.openbanking.model.account_type;

import com.openbanking.comon.SearchCriteria;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.time.OffsetDateTime;
import java.util.Date;

@Data
public class SearchAccountTypeRQ extends SearchCriteria {
    @Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}", message = "Date must be in the format dd-MM-yyyy")
    private String date;
}

