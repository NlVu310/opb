package com.openbanking.service.impl;

import com.openbanking.comon.*;
import com.openbanking.entity.AccountEntity;
import com.openbanking.entity.AccountTypeEntity;
import com.openbanking.entity.AccountTypePermissionEntity;

import com.openbanking.entity.PermissionEntity;
import com.openbanking.exception.DeleteException;
import com.openbanking.exception.ResourceNotFoundException;
import com.openbanking.mapper.AccountTypeMapper;

import com.openbanking.mapper.PermissionMapper;
import com.openbanking.model.account_type.*;


import com.openbanking.model.permission.Permission;
import com.openbanking.repository.AccountRepository;
import com.openbanking.repository.AccountTypePermissionRepository;
import com.openbanking.repository.AccountTypeRepository;
import com.openbanking.repository.PermissionRepository;
import com.openbanking.service.AccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountTypeServiceImpl extends BaseServiceImpl<AccountTypeEntity, AccountType, CreateAccountType, UpdateAccountType, Long> implements AccountTypeService {
    @Autowired
    private AccountTypeRepository accountTypeRepository;
    @Autowired
    private AccountTypeMapper accountTypeMapper;
    @Autowired
    private AccountTypePermissionRepository accountTypePermissionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private PermissionMapper permissionMapper;

    public AccountTypeServiceImpl(BaseRepository<AccountTypeEntity, Long> repository, BaseMapper<AccountTypeEntity, AccountType, CreateAccountType, UpdateAccountType> mapper) {
        super(repository, mapper);
    }

    public PaginationRS<AccountTypeInfo> getListAccountTypeByAccountId(Long accountId, SearchCriteria searchCriteria) {
        Pageable pageable = PageRequest.of(
                searchCriteria != null && searchCriteria.getPage() != null ? searchCriteria.getPage() : 0,
                searchCriteria != null && searchCriteria.getSize() != null ? searchCriteria.getSize() : 10,
                searchCriteria != null && searchCriteria.getSortDirection() != null ? Sort.Direction.fromString(searchCriteria.getSortDirection()) : Sort.Direction.DESC,
                searchCriteria != null && searchCriteria.getSortBy() != null ? searchCriteria.getSortBy() : "id"
        );

        Page<AccountTypeEntity> page;

        if (accountId == null) {
            List<AccountTypeEntity> allEntities = new ArrayList<>(accountTypeRepository.findAll());
            page = new PageImpl<>(allEntities, pageable, allEntities.size());
        } else if (searchCriteria == null) {
            page = accountTypeRepository.getListAccountTypeByAccountId(accountId, pageable);
        } else {
            OffsetDateTime termDate = null;
            try {
                termDate = OffsetDateTime.parse(searchCriteria.getTerm());
            } catch (DateTimeParseException e) {
                // Term không phải là ngày, giữ termDate là null
            }

            page = accountTypeRepository.searchAccountTypes(
                    accountId,
                    termDate != null ? null : searchCriteria.getTerm(),
                    termDate,
                    pageable
            );
        }

        List<AccountTypeInfo> content = page.getContent().stream()
                .map(entity -> {
                    AccountTypeInfo dto = accountTypeMapper.toInfo(entity);
                    if (entity.getCreatedBy() != null) {
                        AccountEntity accountEntity = accountRepository.findByIdAndDeletedAtNull(entity.getCreatedBy());
                        if (accountEntity != null) {
                            dto.setCreatedByName(accountEntity.getName());
                        }
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        PaginationRS<AccountTypeInfo> response = new PaginationRS<>();
        response.setContent(content);
        response.setPageNumber(page.getNumber() + 1);
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
    }



    @Override
    public void create(CreateAccountType createAccountType) {
        try {
            AccountTypeEntity account = accountTypeMapper.toEntityFromCD(createAccountType);
            accountTypeRepository.save(account);

            List<Long> permissionIds = createAccountType.getPermissionIds();
            List<AccountTypePermissionEntity> accountTypePermissionEntities = new ArrayList<>();
            for (Long p : permissionIds) {
                AccountTypePermissionEntity accountTypePermission = new AccountTypePermissionEntity();
                accountTypePermission.setAccountTypeId(account.getId());
                accountTypePermission.setPermissionId(p);
                accountTypePermissionEntities.add(accountTypePermission);
            }
            accountTypePermissionRepository.saveAll(accountTypePermissionEntities);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create AccountType", e);
        }
    }

    @Transactional
    public void update(UpdateAccountType updateAccountType) {
        try {
            AccountTypeEntity entity = accountTypeRepository.findById(updateAccountType.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("AccountType not found with id " + updateAccountType.getId()));
            accountTypeMapper.updateEntityFromDTO(updateAccountType, entity);
            accountTypeRepository.save(entity);

            accountTypePermissionRepository.deleteByAccountTypeId(entity.getId());

            List<Long> permissionIds = updateAccountType.getPermissionIds();
            List<AccountTypePermissionEntity> accountTypePermissionEntities = new ArrayList<>();
            for (Long p : permissionIds) {
                AccountTypePermissionEntity accountTypePermission = new AccountTypePermissionEntity();
                accountTypePermission.setAccountTypeId(entity.getId());
                accountTypePermission.setPermissionId(p);
                accountTypePermissionEntities.add(accountTypePermission);
            }
            accountTypePermissionRepository.saveAll(accountTypePermissionEntities);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update AccountType", e);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        var accountEntity = accountRepository.findByAccountTypeIdAndDeletedAtNull(id);
        if (!accountEntity.isEmpty()) {
            throw new DeleteException("Cannot delete AccountType with ID " + id + " because it is in use.");
        }
        try {
            accountTypePermissionRepository.deleteByAccountTypeId(id);
            accountTypeRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete AccountType", e);
        }
    }

    @Override
    public AccountTypeDetail getAccountTypeDetail(Long id) {
        try {
            AccountTypeEntity accountTypeEntity = accountTypeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("AccountType not found with id " + id));
            AccountTypeDetail accountTypeDetail = accountTypeMapper.toDetail(accountTypeEntity);

            List<PermissionEntity> permissionEntities = permissionRepository.findPermissionsByAccountTypeId(id);
            List<Permission> permissions = permissionMapper.toDTOs(permissionEntities);
            accountTypeDetail.setPermissions(permissions);

            return accountTypeDetail;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch AccountTypeDetail", e);
        }
    }

    @Override
    public AccountTypeDetail getAccountTypeDetail(Long id) {
        AccountTypeEntity accountTypeEntity = accountTypeRepository.findById(id).orElseThrow();
        AccountTypeDetail accountTypeDetail = accountTypeMapper.toDetail(accountTypeEntity);
        List<PermissionEntity> permissionEntities = permissionRepository.findPermissionsByAccountTypeId(id);
        List<Permission> permissions = permissionMapper.toDTOs(permissionEntities);
        accountTypeDetail.setPermissions(permissions);
        return accountTypeDetail;
    }
}

