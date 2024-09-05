package com.openbanking.service.impl;

import com.openbanking.comon.*;
import com.openbanking.entity.AccountEntity;
import com.openbanking.entity.AccountTypeEntity;
import com.openbanking.entity.AccountTypePermissionEntity;
import com.openbanking.entity.PermissionEntity;
import com.openbanking.exception.delete_exception.DeleteExceptionEnum;
import com.openbanking.exception.delete_exception.DeleteExceptionService;
import com.openbanking.exception.insert_exception.InsertExceptionEnum;
import com.openbanking.exception.insert_exception.InsertExceptionService;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionEnum;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionService;
import com.openbanking.mapper.AccountTypeMapper;
import com.openbanking.mapper.PermissionMapper;
import com.openbanking.model.account_type.*;
import com.openbanking.model.permission.Permission;
import com.openbanking.model.permission.PermissionRS;
import com.openbanking.repository.AccountRepository;
import com.openbanking.repository.AccountTypePermissionRepository;
import com.openbanking.repository.AccountTypeRepository;
import com.openbanking.repository.PermissionRepository;
import com.openbanking.service.AccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
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

    @Override
    public PaginationRS<AccountTypeInfo> getListAccountType(SearchAccountTypeRQ searchRQ) {
        Pageable pageable = PageRequest.of(
                searchRQ != null && searchRQ.getPage() != null ? searchRQ.getPage() : 0,
                searchRQ != null && searchRQ.getSize() != null ? searchRQ.getSize() : 10,
                searchRQ != null && searchRQ.getSortDirection() != null ? Sort.Direction.fromString(searchRQ.getSortDirection()) : Sort.Direction.DESC,
                searchRQ != null && searchRQ.getSortBy() != null ? searchRQ.getSortBy() : "id"
        );

        Page<AccountTypeEntity> page;

        if (searchRQ == null) {
            page = accountTypeRepository.getListAccountType(pageable);
        } else {
            String term = searchRQ.getTerm();
            LocalDate date = null;
            if (searchRQ.getCreatedAt() != null) {
                try {
                    date = LocalDate.parse(searchRQ.getCreatedAt(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid date format. Please use dd-MM-yyyy.");
                }
            }

            page = accountTypeRepository.searchAccountTypes(
                    term,
                    date,
                    pageable
            );
        }

        List<Long> createdByIds = page.getContent().stream()
                .map(AccountTypeEntity::getCreatedBy)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<AccountEntity> accountEntities = accountRepository.findAllByIdInAndDeletedAtNull(createdByIds);

        Map<Long, AccountEntity> accountEntityMap = accountEntities.stream()
                .collect(Collectors.toMap(AccountEntity::getId, Function.identity()));

        List<AccountTypeInfo> content = page.getContent().stream()
                .map(accountTypeEntity -> {
                    AccountTypeInfo dto = accountTypeMapper.toInfo(accountTypeEntity);
                    if (accountTypeEntity.getCreatedBy() != null) {
                        AccountEntity accountEntity = accountEntityMap.get(accountTypeEntity.getCreatedBy());
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

        return response;
    }




    @Override
    public void createAccountType(CreateAccountType createAccountType, Long accountId) {
        try {
            List<AccountTypeEntity> accountTypeEntities = accountTypeRepository.findAll();
            List<String> accountTypeNames = accountTypeEntities.stream()
                    .map(AccountTypeEntity::getName)
                    .collect(Collectors.toList());
            if (accountTypeNames.contains(createAccountType.getName()))
                throw new InsertExceptionService(InsertExceptionEnum.INSERT_USER_NAME_ERROR, "");
            AccountTypeEntity accountType = accountTypeMapper.toEntityFromCD(createAccountType);
            accountType.setCreatedBy(accountId);
            accountTypeRepository.save(accountType);

            List<Long> permissionIds = createAccountType.getPermissionIds();
            List<AccountTypePermissionEntity> accountTypePermissionEntities = new ArrayList<>();
            for (Long p : permissionIds) {
                AccountTypePermissionEntity accountTypePermission = new AccountTypePermissionEntity();
                accountTypePermission.setAccountTypeId(accountType.getId());
                accountTypePermission.setPermissionId(p);
                accountTypePermissionEntities.add(accountTypePermission);
            }
            accountTypePermissionRepository.saveAll(accountTypePermissionEntities);
        } catch (Exception e) {
            throw new InsertExceptionService(InsertExceptionEnum.INSERT_ACC_TYPE_ERROR, "");
        }
    }

    @Transactional
    public void update(UpdateAccountType updateAccountType) {
        try {
            AccountTypeEntity entity = accountTypeRepository.findById(updateAccountType.getId())
                    .orElseThrow(() -> new ResourceNotFoundExceptionService( ResourceNotFoundExceptionEnum.RNF_ACC_TYPE, "with id " + updateAccountType.getId()));
            accountTypeMapper.updateEntityFromUDTO(updateAccountType, entity);
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
        } catch (Exception e) {
            throw new InsertExceptionService(InsertExceptionEnum.INSERT_UPDATE_ACC_TYPE_ERROR , "");
        }
    }

    @Transactional
    public void deleteById(Long id) {
        var accountEntity = accountRepository.findByAccountTypeIdAndDeletedAtNull(id);
        if (!accountEntity.isEmpty()) {
            throw new DeleteExceptionService(DeleteExceptionEnum.DELETE_ACC_ACC_TYPE_ERROR, "");
        }
        try {
            accountTypePermissionRepository.deleteByAccountTypeId(id);
            accountTypeRepository.deleteById(id);
        } catch (Exception e) {
            throw new DeleteExceptionService(DeleteExceptionEnum.DELETE_ACC_TYPE_ERROR, "");
        }
    }

    @Override
    public AccountTypeDetail getAccountTypeDetail(Long id) {
        try {
            AccountTypeEntity accountTypeEntity = accountTypeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundExceptionService( ResourceNotFoundExceptionEnum.RNF_ACC_TYPE, "with id " + id));

            AccountTypeDetail accountTypeDetail = accountTypeMapper.toDetail(accountTypeEntity);

            List<PermissionEntity> permissionEntities = permissionRepository.findPermissionsByAccountTypeId(id);

            List<Permission> permissions = permissionMapper.toDTOs(permissionEntities);

            PermissionRS permissionRS = PermissionRS.convertToPermissionRS(permissions);

            accountTypeDetail.setPermissions(permissionRS);

            return accountTypeDetail;
        } catch (Exception e) {
            throw new ResourceNotFoundExceptionService(ResourceNotFoundExceptionEnum.RNF_ACC_TYPE, "");
        }
    }

    @Override
    public List<AccountTypeInfo> getAllAccountTypeInfo() {
        try {
            List<AccountTypeEntity> accountTypeEntities = accountTypeRepository.getListAccountTypeInfo();
            List<Long> accountTypeIds = accountTypeEntities.stream()
                    .map(AccountTypeEntity::getId)
                    .collect(Collectors.toList());

            List<AccountTypePermissionEntity> accountTypePermissionEntities = accountTypePermissionRepository.findByAccountTypeIdIn(accountTypeIds);
            List<Long> permissionIds = accountTypePermissionEntities.stream()
                    .map(AccountTypePermissionEntity::getPermissionId)
                    .collect(Collectors.toList());

            List<PermissionEntity> permissionEntities = permissionRepository.findByIdIn(permissionIds);
            Map<Long, List<PermissionEntity>> permissionsByAccountTypeId = accountTypePermissionEntities.stream()
                    .collect(Collectors.groupingBy(
                            AccountTypePermissionEntity::getAccountTypeId,
                            Collectors.mapping(e -> permissionEntities.stream()
                                    .filter(p -> p.getId().equals(e.getPermissionId()))
                                    .findFirst()
                                    .orElse(null), Collectors.toList())
                    ));

            List<AccountTypeInfo> accountTypeInfos = new ArrayList<>();
            for (AccountTypeEntity accountTypeEntity : accountTypeEntities) {
                AccountTypeInfo accountTypeInfo = accountTypeMapper.toInfo(accountTypeEntity);
                List<PermissionEntity> permissions = permissionsByAccountTypeId.getOrDefault(accountTypeEntity.getId(), Collections.emptyList());
                List<Permission> permissionDTOs = permissionMapper.toDTOs(permissions);
                PermissionRS permissionRS = PermissionRS.convertToPermissionRS(permissionDTOs);

                accountTypeInfo.setPermissions(permissionRS);
                accountTypeInfos.add(accountTypeInfo);
            }
            return accountTypeInfos;

        } catch (Exception e) {
            throw new ResourceNotFoundExceptionService(ResourceNotFoundExceptionEnum.RNF_ACC_TYPE, "");
        }
    }
}