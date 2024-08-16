package com.openbanking.model.system_configuration_transaction_content;

public interface SystemConfigurationTransactionContentProjection {
    Long getId();
    Long getCustomerId();
    Long getSource();
    Long getRefNoLength();
    Long getRefNoStart();
    Long getRefNoEnd();
    String getCustomerName();
}
