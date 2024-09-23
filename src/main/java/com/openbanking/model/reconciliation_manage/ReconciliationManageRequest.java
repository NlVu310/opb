package com.openbanking.model.reconciliation_manage;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ReconciliationManageRequest {
    private Long partnerId;
    private OffsetDateTime fromDate;
    private OffsetDateTime toDate;
}
