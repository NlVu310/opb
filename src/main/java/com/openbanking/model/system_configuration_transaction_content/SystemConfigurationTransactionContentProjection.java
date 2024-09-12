package com.openbanking.model.system_configuration_transaction_content;

public interface SystemConfigurationTransactionContentProjection {
    Long getId();
    Long getCustomerId();
    String getSourceStart();
    Long getSourceLengthEnd();
    String getSourceIndexEnd();
    String getSourceRegex();
    String getRefNoStart();
    Long getRefNoLengthEnd();
    String getRefNoIndexEnd();
    String getRefNoRegex();
    String getCustomerName();
}
