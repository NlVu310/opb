package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.SystemConfigurationTransactionContentEntity;
import com.openbanking.model.system_configuration_transaction_content.CreateSystemConfigurationTransactionContent;
import com.openbanking.model.system_configuration_transaction_content.SystemConfigurationTransactionContent;
import com.openbanking.model.system_configuration_transaction_content.SystemConfigurationTransactionContentProjection;
import com.openbanking.model.system_configuration_transaction_content.UpdateSystemConfigurationTransactionContent;
import org.mapstruct.Mapper;

@Mapper
public interface SystemConfigurationTransactionContentMapper extends BaseMapper<SystemConfigurationTransactionContentEntity, SystemConfigurationTransactionContent, CreateSystemConfigurationTransactionContent, UpdateSystemConfigurationTransactionContent> {
    SystemConfigurationTransactionContent getDTO(SystemConfigurationTransactionContentProjection transactionContentProjection);
}
