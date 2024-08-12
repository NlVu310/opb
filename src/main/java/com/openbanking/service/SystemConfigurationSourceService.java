package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.model.account_type.CreateAccountType;
import com.openbanking.model.account_type.UpdateAccountType;
import com.openbanking.model.system_configuration_source.CreateSystemConfigurationSource;
import com.openbanking.model.system_configuration_source.SystemConfigurationSource;
import com.openbanking.model.system_configuration_source.UpdateSystemConfigurationSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SystemConfigurationSourceService extends BaseService<SystemConfigurationSource, CreateSystemConfigurationSource, UpdateSystemConfigurationSource, Long> {
    void create(CreateSystemConfigurationSource createSystemConfigurationSource);

    void deleteListById(List<Long> ids);

}
