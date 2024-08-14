package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.SystemConfigurationAutoReconciliationEntity;
import com.openbanking.model.system_configuration_auto_reconciliation.CreateReconciliationRQ;
import com.openbanking.model.system_configuration_auto_reconciliation.CreateSystemConfigurationAutoReconciliation;
import com.openbanking.model.system_configuration_auto_reconciliation.SystemConfigurationAutoReconciliation;
import com.openbanking.model.system_configuration_auto_reconciliation.UpdateSystemConfigurationAutoReconciliation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalTime;

@Mapper(config = GlobalMapperConfig.class)
public interface SystemConfigurationAutoReconciliationMapper extends BaseMapper<SystemConfigurationAutoReconciliationEntity, SystemConfigurationAutoReconciliation, CreateSystemConfigurationAutoReconciliation, UpdateSystemConfigurationAutoReconciliation> {

    @Mapping(source = "reconciliationTime", target = "reconciliationTime", qualifiedByName = "localTimeToString")
    @Mapping(source = "sourceId", target = "source.id")
    @Mapping(source = "sourceCode", target = "source.code")
    @Mapping(source = "partnerName", target = "partner.name")
    SystemConfigurationAutoReconciliation toDTO(SystemConfigurationAutoReconciliationEntity entity);


    @Mapping(source = "reconciliationTime", target = "reconciliationTime", qualifiedByName = "stringToLocalTime")
    SystemConfigurationAutoReconciliationEntity getEntity(CreateReconciliationRQ rq);

    @Named("localTimeToString")
    default String localTimeToString(LocalTime localTime) {
        return localTime != null ? localTime.toString() : null;
    }

    @Named("stringToLocalTime")
    default LocalTime stringToLocalTime(String localTimeStr) {
        return localTimeStr != null ? LocalTime.parse(localTimeStr) : null;
    }
}

