package com.openbanking.model.reconciliation_manage;

import lombok.Data;

@Data
public class FeignResult {
    private Integer statusCode;
    private String message;
    private TestResult result;
}
