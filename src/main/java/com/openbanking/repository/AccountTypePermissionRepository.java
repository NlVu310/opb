package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.AccountTypePermissionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountTypePermissionRepository extends BaseRepository<AccountTypePermissionEntity, Long> {
    List<AccountTypePermissionEntity> findByAccountTypeId(Long accountTypeId);

    List<AccountTypePermissionEntity> findByAccountTypeIdIn(List<Long> accountTypeId);

    void deleteByAccountTypeId(Long accountTypeId);
}