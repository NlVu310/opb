package com.openbanking.model.system_configuration_source;

public interface SystemConfigurationSourceProjection {
    Long getId();
    String getCode();
    String getInfo();
    String getDescription();
    String getStatus();
    String getPartnerName();
}
