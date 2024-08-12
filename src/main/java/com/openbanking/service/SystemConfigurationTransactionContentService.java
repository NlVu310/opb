package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.model.system_configuration_transaction_content.CreateSystemConfigurationTransactionContent;
import com.openbanking.model.system_configuration_transaction_content.SystemConfigurationTransactionContent;
import com.openbanking.model.system_configuration_transaction_content.UpdateSystemConfigurationTransactionContent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SystemConfigurationTransactionContentService extends BaseService<SystemConfigurationTransactionContent, CreateSystemConfigurationTransactionContent, UpdateSystemConfigurationTransactionContent, Long> {
    void deleteListById(List<Long> ids);

    SystemConfigurationTransactionContent getById(Long id);

}
