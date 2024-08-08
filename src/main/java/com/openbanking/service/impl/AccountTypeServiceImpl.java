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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
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
    public PaginationRS<AccountTypeInfo> getListAccountTypeByAccountId(Long id, SearchCriteria searchCriteria) {
        return null;
    }


    @Override
    public void create(CreateAccountType createAccountType) {
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
    }

    @Transactional
    public void update(UpdateAccountType updateAccountType) {
        AccountTypeEntity entity = accountTypeRepository.findById(updateAccountType.getId()).orElseThrow(() -> new RuntimeException("Entity not found"));
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
    }


    @Transactional
    public void deleteById(Long id) {
        var accountEntity = accountRepository.findByAccountTypeIdAndDeletedAtNull(id);
        if (!accountEntity.isEmpty()) {
            throw new DeleteException("AccountType with ID " + id + " existed");
        }
        accountTypePermissionRepository.deleteByAccountTypeId(id);
        accountTypeRepository.deleteById(id);
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
