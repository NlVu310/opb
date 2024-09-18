package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.comon.PaginationRS;
import com.openbanking.entity.AccountEntity;
import com.openbanking.entity.PartnerEntity;
import com.openbanking.entity.SystemConfigurationSourceEntity;
import com.openbanking.exception.delete_exception.DeleteExceptionEnum;
import com.openbanking.exception.delete_exception.DeleteException;
import com.openbanking.exception.insert_exception.InsertExceptionEnum;
import com.openbanking.exception.insert_exception.InsertException;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionEnum;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundException;
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
                    .orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_PARTNER, "with id " + id));

            List<SystemConfigurationSourceEntity> sourceEntities = systemConfigurationSourceRepository.getListSourceByPartnerId(id);

            PartnerDetail partnerDetail = partnerMapper.getDetail(partnerEntity);

            List<SystemConfigurationSource> sources = sourceEntities.stream()
                    .map(systemConfigurationSourceMapper::toDTO)
                    .collect(Collectors.toList());

            partnerDetail.setSources(sources);

            return partnerDetail;
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }

    @Override
    public void create(CreatePartner createPartner) {
        try {
            List<String> names = partnerRepository.findDistinctNames();
            if (names.contains(createPartner.getName())) {
                throw new InsertException(InsertExceptionEnum.INSERT_VLD_PAR_ERROR,"");
            }
            PartnerEntity partnerEntity = partnerMapper.toEntityFromCD(createPartner);
            partnerRepository.save(partnerEntity);
        } catch (InsertException e) {
            throw e;
        }
    }

    @Override
    public void update(UpdatePartner updatePartner) {
        try {
            PartnerEntity existingPartner = partnerRepository.findById(updatePartner.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_PARTNER, ""));

            String newPartnerName = updatePartner.getName().toLowerCase();

            List<String> existingPartnerNames = partnerRepository.findDistinctLowercasePartnerNamesExcluding(updatePartner.getId());
            boolean isNameExists = existingPartnerNames.contains(newPartnerName);
            if (isNameExists) {
                throw new InsertException(InsertExceptionEnum.INSERT_VLD_PAR_ERROR,"");
            }
            partnerMapper.updateEntityFromUDTO(updatePartner, existingPartner);
            partnerRepository.save(existingPartner);

        } catch (ResourceNotFoundException | InsertException e) {
            throw e;
        } catch (Exception e) {
            throw new InsertException(InsertExceptionEnum.INSERT_UDP_PAR_ERROR ,"");
        }
    }

    @Override
    public PaginationRS<Partner> getListPartnerByAccount(Long accountId, SearchPartnerRQ searchRQ) {
        if (searchRQ == null) {
            searchRQ = new SearchPartnerRQ();
        }

        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_ACC ,"with id " + accountId));

        List<Long> partnerConcernedIds = account.getPartnerConcerned();
        if (partnerConcernedIds == null || partnerConcernedIds.isEmpty()) {
            if (account.getCustomerConcerned() == null || account.getCustomerConcerned().isEmpty()) {
                partnerConcernedIds = partnerRepository.getListPartnerId();
            } else partnerConcernedIds = Collections.emptyList();
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
            throw new DeleteException(DeleteExceptionEnum.DELETE_PAR_BANK_ERROR , "");
        }

        List<Long> sourceIds = systemConfigurationSourceRepository.getListSourceIdByPartnerIds(ids);
        if (!sourceIds.isEmpty()) {
            throw new DeleteException(DeleteExceptionEnum.DELETE_PAR_SOURCE_ERROR, "");
        }

        List<PartnerEntity> partnerEntities = partnerRepository.findByIdIn(ids);
        if (!partnerEntities.isEmpty()) {
            OffsetDateTime now = OffsetDateTime.now();
            partnerEntities.forEach(partnerEntity -> partnerEntity.setDeletedAt(now));

            try {
                partnerRepository.saveAll(partnerEntities);
            } catch (DeleteException e){
                throw e;
            }
            catch (Exception e) {
                throw new DeleteException(DeleteExceptionEnum.DELETE_PARTNER, "");
            }
        }
    }

}


