package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.SystemConfigurationAutoReconciliationEntity;
import com.openbanking.entity.SystemConfigurationSourceEntity;
import com.openbanking.exception.DeleteException;
import com.openbanking.exception.InsertException;
import com.openbanking.mapper.SystemConfigurationAutoReconciliationMapper;
import com.openbanking.model.system_configuration_auto_reconciliation.CreateReconciliationRQ;
import com.openbanking.model.system_configuration_auto_reconciliation.CreateSystemConfigurationAutoReconciliation;
import com.openbanking.model.system_configuration_auto_reconciliation.SystemConfigurationAutoReconciliation;
import com.openbanking.model.system_configuration_auto_reconciliation.UpdateSystemConfigurationAutoReconciliation;
import com.openbanking.repository.PartnerRepository;
import com.openbanking.repository.SystemConfigurationAutoReconciliationRepository;
import com.openbanking.repository.SystemConfigurationSourceRepository;
import com.openbanking.service.SystemConfigurationAutoReconciliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

            List<SystemConfigurationSourceEntity> systemConfigurationSourceEntities = systemConfigurationSourceRepository.findAllByCodeIn(sourceCodes);
            Map<String, Long> sourceIdToCodeMap = systemConfigurationSourceEntities.stream()
                    .collect(Collectors.toMap(SystemConfigurationSourceEntity::getCode, SystemConfigurationSourceEntity::getId));

            Set<Long> partnerIds = systemConfigurationSourceEntities.stream().map(SystemConfigurationSourceEntity::getPartnerId).collect(Collectors.toSet());
            if (partnerIds.size() > 1)
                throw new InsertException("List sourceCode " + sourceCodes + "cannot have more than 1 partner");
            if (partnerIds.isEmpty())
                throw new InsertException("List sourceCode " + sourceCodes + "don't have any partner");
            String partnerName = partnerRepository.getPartnerNameById(partnerIds.iterator().next());

            List<SystemConfigurationAutoReconciliationEntity> entities = reconciliationRQs.stream()
                    .map(dtoItem -> {
                        SystemConfigurationAutoReconciliationEntity entity = systemConfigurationAutoReconciliationMapper.getEntity(dtoItem);
                        entity.setPartnerName(partnerName);
                        entity.setSourceId(sourceIdToCodeMap.get(dtoItem.getSourceCode()));
                        return entity;
                    })
                    .collect(Collectors.toList());

            systemConfigurationAutoReconciliationRepository.saveAll(entities);
        } catch (InsertException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Reconciliation", e);
        }
    }


    @Override
    public void deleteListById(List<Long> ids) {
        try {
            systemConfigurationAutoReconciliationRepository.deleteAllById(ids);
        } catch (DeleteException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Reconciliation", e);
        }
    }

    @Override
    public SystemConfigurationAutoReconciliation getDetailById(Long id) {
        var configurationAutoReconciliation = this.getById(id);
        Long partnerId = partnerRepository.getPartnerNameByReconciliationId(id);
        configurationAutoReconciliation.getPartner().setId(partnerId);
        return configurationAutoReconciliation;
    }
}
