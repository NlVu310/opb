package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.SystemConfigurationAutoReconciliationEntity;
import com.openbanking.entity.SystemConfigurationSourceEntity;
import com.openbanking.exception.delete_exception.DeleteExceptionEnum;
import com.openbanking.exception.delete_exception.DeleteException;
import com.openbanking.exception.insert_exception.InsertExceptionEnum;
import com.openbanking.exception.insert_exception.InsertException;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionEnum;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundException;
import com.openbanking.mapper.SystemConfigurationAutoReconciliationMapper;
import com.openbanking.model.system_configuration_auto_reconciliation.*;
import com.openbanking.repository.PartnerRepository;
import com.openbanking.repository.SystemConfigurationAutoReconciliationRepository;
import com.openbanking.repository.SystemConfigurationSourceRepository;
import com.openbanking.service.SystemConfigurationAutoReconciliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SystemConfigurationAutoReconciliationServiceImpl extends BaseServiceImpl<SystemConfigurationAutoReconciliationEntity, SystemConfigurationAutoReconciliation, CreateSystemConfigurationAutoReconciliation, UpdateSystemConfigurationAutoReconciliation, Long> implements SystemConfigurationAutoReconciliationService {
    @Autowired
    private SystemConfigurationAutoReconciliationRepository systemConfigurationAutoReconciliationRepository;
    @Autowired
    private SystemConfigurationAutoReconciliationMapper systemConfigurationAutoReconciliationMapper;
    @Autowired
    private SystemConfigurationSourceRepository systemConfigurationSourceRepository;
    @Autowired
    private PartnerRepository partnerRepository;

    public SystemConfigurationAutoReconciliationServiceImpl(BaseRepository<SystemConfigurationAutoReconciliationEntity, Long> repository, BaseMapper<SystemConfigurationAutoReconciliationEntity, SystemConfigurationAutoReconciliation, CreateSystemConfigurationAutoReconciliation, UpdateSystemConfigurationAutoReconciliation> mapper) {
        super(repository, mapper);
    }

    @Override
    public void create(CreateSystemConfigurationAutoReconciliation createSystemConfigurationAutoReconciliation) {
        try {
            List<CreateReconciliationRQ> reconciliationRQs = createSystemConfigurationAutoReconciliation.getReconciliationRQs();

            List<String> sourceCodes = reconciliationRQs.stream()
                    .map(CreateReconciliationRQ::getSourceCode)
                    .distinct()
                    .collect(Collectors.toList());

            List<Long> reconciliationIds = reconciliationRQs.stream()
                    .map(CreateReconciliationRQ::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            List<SystemConfigurationSourceEntity> systemConfigurationSourceEntities = systemConfigurationSourceRepository.findAllByCodeIn(sourceCodes);
            Set<Long> partnerIds = systemConfigurationSourceEntities.stream()
                    .map(SystemConfigurationSourceEntity::getPartnerId)
                    .collect(Collectors.toSet());

            if (partnerIds.isEmpty()) {
                throw new InsertException( InsertExceptionEnum.INSERT_SOURCE_ERROR, "List sourceCode " + sourceCodes + " don't have any partner");
            }
            if (partnerIds.size() > 1) {
                throw new InsertException( InsertExceptionEnum.INSERT_SOURCE_ERROR,"List sourceCode " + sourceCodes + " cannot have more than 1 partner");
            }

            Long partnerId = partnerIds.iterator().next();
            String partnerName = partnerRepository.getPartnerNameById(partnerId);

            Map<String, Long> sourceIdToCodeMap = systemConfigurationSourceEntities.stream()
                    .collect(Collectors.toMap(SystemConfigurationSourceEntity::getCode, SystemConfigurationSourceEntity::getId));

            Map<Long, SystemConfigurationAutoReconciliationEntity> existingEntities = systemConfigurationAutoReconciliationRepository
                    .findAllById(reconciliationIds)
                    .stream()
                    .collect(Collectors.toMap(SystemConfigurationAutoReconciliationEntity::getId, Function.identity()));

            List<SystemConfigurationAutoReconciliationEntity> entitiesToSave = reconciliationRQs.stream()
                    .map(dtoItem -> {
                        SystemConfigurationAutoReconciliationEntity entity = existingEntities.getOrDefault(
                                dtoItem.getId(),
                                systemConfigurationAutoReconciliationMapper.getEntity(dtoItem)
                        );

                        entity.setPartnerName(partnerName);
                        entity.setSourceId(sourceIdToCodeMap.get(dtoItem.getSourceCode()));
                        entity.setReconciliationTime(LocalTime.parse(dtoItem.getReconciliationTime()));
                        entity.setReconciliationFrequencyNumber(dtoItem.getReconciliationFrequencyNumber());
                        entity.setReconciliationFrequencyUnit(dtoItem.getReconciliationFrequencyUnit());
                        entity.setRetryTimeNumber(dtoItem.getRetryTimeNumber());
                        entity.setRetryFrequencyNumber(dtoItem.getRetryFrequencyNumber());

                        return entity;
                    })
                    .collect(Collectors.toList());

            systemConfigurationAutoReconciliationRepository.saveAll(entitiesToSave);

        } catch (InsertException e) {
            throw e;
        }
    }

    @Override
    public void deleteListById(List<Long> ids) {
        try {
            systemConfigurationAutoReconciliationRepository.deleteAllById(ids);
        } catch (Exception e ) {
            throw new DeleteException(DeleteExceptionEnum.DELETE_SYS_RECON_ERROR, "");
        }
    }

    @Override
    public SystemConfigurationAutoReconciliation getDetailById(Long id) {
        try {
            var configurationAutoReconciliation = this.getById(id);
            Long partnerId = partnerRepository.getPartnerNameByReconciliationId(id);
            configurationAutoReconciliation.getPartner().setId(partnerId);
            return configurationAutoReconciliation;
        } catch (Exception e) {
            throw new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_RECON ,"");
        }
    }

    @Override
    public List<SystemConfigurationAutoReconciliation> getListByPartnerId(Long id) {
        List<SystemConfigurationAutoReconciliationProjection> projections = systemConfigurationAutoReconciliationRepository.getListByPartnerId(id);
        return projections.stream().map(systemConfigurationAutoReconciliationMapper::getDTO).collect(Collectors.toList());
    }
}
