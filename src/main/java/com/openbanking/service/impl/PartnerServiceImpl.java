package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.comon.PaginationRS;
import com.openbanking.entity.CustomerEntity;
import com.openbanking.entity.PartnerEntity;
import com.openbanking.entity.SystemConfigurationSourceEntity;
import com.openbanking.exception.DeleteException;
import com.openbanking.exception.InsertException;
import com.openbanking.exception.ResourceNotFoundException;
import com.openbanking.mapper.PartnerMapper;
import com.openbanking.mapper.SystemConfigurationSourceMapper;
import com.openbanking.model.partner.*;
import com.openbanking.model.system_configuration_source.SystemConfigurationSource;
import com.openbanking.repository.BankAccountRepository;
import com.openbanking.repository.PartnerRepository;
import com.openbanking.repository.SystemConfigurationSourceRepository;
import com.openbanking.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartnerServiceImpl extends BaseServiceImpl<PartnerEntity, Partner, CreatePartner, UpdatePartner, Long> implements PartnerService {
    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private SystemConfigurationSourceRepository systemConfigurationSourceRepository;
    @Autowired
    private PartnerMapper partnerMapper;
    @Autowired
    private SystemConfigurationSourceMapper systemConfigurationSourceMapper;

    @Autowired
    private BankAccountRepository bankAccountRepository;
    public PartnerServiceImpl(BaseRepository<PartnerEntity, Long> repository, BaseMapper<PartnerEntity, Partner, CreatePartner, UpdatePartner> mapper) {
        super(repository, mapper);
    }

    @Override
    public PartnerDetail getDetailById(Long id) {
        try {
            PartnerEntity partnerEntity = partnerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Partner not found with id " + id));

            List<SystemConfigurationSourceEntity> sourceEntities = systemConfigurationSourceRepository.getListSourceByPartnerId(id);

            PartnerDetail partnerDetail = partnerMapper.getDetail(partnerEntity);

            List<SystemConfigurationSource> sources = sourceEntities.stream()
                    .map(systemConfigurationSourceMapper::toDTO)
                    .collect(Collectors.toList());

            partnerDetail.setSources(sources);

            return partnerDetail;
        }catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Partner Detail", e);
        }
    }

    @Override
    public PaginationRS<Partner> getListPartner(SearchPartnerRQ searchRQ) {
        if (searchRQ == null) {
            List<PartnerEntity> partnerEntities = partnerRepository.findAll();

            List<Partner> partners = partnerEntities.stream()
                    .map(partnerMapper::toDTO)
                    .collect(Collectors.toList());

            PaginationRS<Partner> result = new PaginationRS<>();
            result.setContent(partners);
            result.setPageNumber(0);
            result.setPageSize(partners.size());
            result.setTotalElements(partners.size());
            result.setTotalPages(1);

            return result;
        }

        Pageable pageable = PageRequest.of(
                searchRQ.getPage() != null ? searchRQ.getPage() : 0,
                searchRQ.getSize() != null ? searchRQ.getSize() : 10,
                Sort.by(Sort.Direction.fromString(
                                searchRQ.getSortDirection() != null ? searchRQ.getSortDirection() : "DESC"),
                        searchRQ.getSortBy() != null ? searchRQ.getSortBy() : "id")
        );

        Page<PartnerEntity> partnerEntities = partnerRepository.searchPartners(searchRQ, searchRQ.getTerm(), pageable);

        List<Partner> partners = partnerEntities.getContent()
                .stream()
                .map(partnerMapper::toDTO)
                .collect(Collectors.toList());

        PaginationRS<Partner> result = new PaginationRS<>();
        result.setContent(partners);
        result.setPageNumber(partnerEntities.getNumber());
        result.setPageSize(partnerEntities.getSize());
        result.setTotalElements(partnerEntities.getTotalElements());
        result.setTotalPages(partnerEntities.getTotalPages());

        return result;
    }


    @Override
    @Transactional
    public void deleteByListId(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        List<Long> bankAccountIds = bankAccountRepository.getListBankAccountIdByPartnerIds(ids);
        if (!bankAccountIds.isEmpty()) {
            throw new DeleteException("Partner has been assigned to the bank account. Delete operation failed.");
        }

        List<Long> sourceIds = systemConfigurationSourceRepository.getListSourceIdByPartnerIds(ids);
        if (!sourceIds.isEmpty()) {
            throw new DeleteException("Partner has been assigned to the source config. Delete operation failed.");
        }

        List<PartnerEntity> partnerEntities = partnerRepository.findByIdIn(ids);
        if (!partnerEntities.isEmpty()) {
            OffsetDateTime now = OffsetDateTime.now();
            partnerEntities.forEach(partnerEntity -> partnerEntity.setDeletedAt(now));

            try {
                partnerRepository.saveAll(partnerEntities);
            } catch (Exception e) {
                throw new InsertException("Insert partner failed");
            }
        }
    }

}


