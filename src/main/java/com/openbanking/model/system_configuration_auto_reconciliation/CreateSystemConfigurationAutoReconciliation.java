package com.openbanking.model.system_configuration_auto_reconciliation;

import lombok.Data;

import java.util.List;

@Data
public class CreateSystemConfigurationAutoReconciliation {
    private String partnerName;
    private List<CreateReconciliationRQ> reconciliationRQS;
}

