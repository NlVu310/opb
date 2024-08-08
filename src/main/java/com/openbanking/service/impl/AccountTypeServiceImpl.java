package com.openbanking.service.impl;

import com.openbanking.comon.BaseDTO;
import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.AccountEntity;
import com.openbanking.entity.AccountTypeEntity;
import com.openbanking.entity.AccountTypePermissionEntity;
import com.openbanking.mapper.AccountTypeMapper;

import com.openbanking.model.account_type.AccountType;
import com.openbanking.model.account_type.CreateAccountType;
import com.openbanking.model.account_type.UpdateAccountType;
//import com.openbanking.repository.AccountTypePermissionRepository;
import com.openbanking.model.account_type_permission.AccountTypePermission;
import com.openbanking.repository.AccountTypePermissionRepository;
import com.openbanking.repository.AccountTypeRepository;
import com.openbanking.service.AccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public AccountTypeServiceImpl(BaseRepository<AccountTypeEntity, Long> repository, BaseMapper<AccountTypeEntity, AccountType, CreateAccountType, UpdateAccountType> mapper) {
        super(repository, mapper);
    }

    @Override
    public List<AccountType> getListAccountTypeById(Long id) {
        List<AccountTypeEntity> accountTypeEntities = accountTypeRepository.getListAccountTypeByAccountId(id);
        var rs = accountTypeEntities.stream().map(accountTypeMapper::toDTO).collect(Collectors.toList());
        return rs;
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

    public void update(UpdateAccountType updateAccountType){
        AccountTypeEntity entity = accountTypeRepository.findById((Long) updateAccountType.getId()).orElseThrow(() -> new RuntimeException("Entity not found"));
        accountTypeMapper.updateEntityFromDTO(updateAccountType , entity);
        accountTypeRepository.save(entity);
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
}
