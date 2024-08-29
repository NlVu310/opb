package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.comon.PaginationRS;
import com.openbanking.entity.AccountEntity;
import com.openbanking.entity.CustomerEntity;
import com.openbanking.entity.PartnerEntity;
import com.openbanking.entity.SystemConfigurationSourceEntity;
import com.openbanking.exception.DeleteException;
import com.openbanking.exception.InsertException;
import com.openbanking.exception.InvalidInputException;
import com.openbanking.exception.ResourceNotFoundException;
import com.openbanking.mapper.PartnerMapper;
import com.openbanking.mapper.SystemConfigurationSourceMapper;
import com.openbanking.model.partner.*;
import com.openbanking.model.system_configuration_source.SystemConfigurationSource;
import com.openbanking.repository.AccountRepository;
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
import java.util.Collections;
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
    @Autowired
    private AccountRepository accountRepository;
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
    public void create(CreatePartner createPartner) {
        try{
            List<String> names = partnerRepository.findDistinctNames();
            if (names.contains(createPartner.getName())) {
                throw new InvalidInputException("PartnerName already exists");
            }
            PartnerEntity partnerEntity = partnerMapper.toEntityFromCD(createPartner);
            partnerRepository.save(partnerEntity);
        }catch (Exception e) {
            throw new RuntimeException("An error occurred while creating partner", e);
        }
    }

    @Override
    public void update(UpdatePartner updatePartner) {
        try {
            PartnerEntity existingPartner = partnerRepository.findById(updatePartner.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Partner not found"));

            String newPartnerName = updatePartner.getName().toLowerCase();

            List<String> existingPartnerNames = partnerRepository.findDistinctLowercasePartnerNamesExcluding(updatePartner.getId());
            boolean isNameExists = existingPartnerNames.contains(newPartnerName);
            if (isNameExists) {
                throw new InvalidInputException("Partner name already exists");
            }
            partnerMapper.updateEntityFromUDTO(updatePartner, existingPartner);
            partnerRepository.save(existingPartner);

        } catch (Exception e) {
            String originalMessage = e.getMessage();
            throw new RuntimeException("Failed to update Partner: " + originalMessage, e);
        }
    }

    @Override
    public PaginationRS<Partner> getListPartnerByAccount(Long accountId, SearchPartnerRQ searchRQ) {
        if (searchRQ == null) {
            searchRQ = new SearchPartnerRQ();
        }

        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + accountId));

        List<Long> partnerConcernedIds = account.getPartnerConcerned();
        if (partnerConcernedIds == null || partnerConcernedIds.isEmpty()) {
            partnerConcernedIds = Collections.emptyList();
        }

        Pageable pageable = PageRequest.of(
                searchRQ.getPage() != null ? searchRQ.getPage() : 0,
                searchRQ.getSize() != null ? searchRQ.getSize() : 10,
                Sort.by(Sort.Direction.fromString(
                                searchRQ.getSortDirection() != null ? searchRQ.getSortDirection() : "DESC"),
                        searchRQ.getSortBy() != null ? searchRQ.getSortBy() : "id")
        );

        Page<PartnerEntity> partnerEntities = partnerRepository.searchPartners(
                searchRQ,
                searchRQ.getTerm(),
                partnerConcernedIds,
                pageable
        );

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


