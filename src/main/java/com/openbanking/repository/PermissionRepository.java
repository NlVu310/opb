package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.AccountTypePermissionEntity;
import com.openbanking.entity.PermissionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends BaseRepository<PermissionEntity, Long> {
    @Query("SELECT p FROM PermissionEntity p WHERE p.id IN (SELECT atp.permissionId FROM AccountTypePermissionEntity atp WHERE atp.accountTypeId = :accountTypeId)")
    List<PermissionEntity> findByAccountTypeId(@Param("accountTypeId") Long accountTypeId);

    @Query("SELECT p " +
            "FROM PermissionEntity p " +
            "JOIN AccountTypePermissionEntity atp ON p.id = atp.permissionId " +
            "WHERE atp.accountTypeId = :accountTypeId")
    List<PermissionEntity> findPermissionsByAccountTypeId(@Param("accountTypeId") Long accountTypeId);

    @Query("SELECT p " +
            "FROM PermissionEntity p " +
            "JOIN AccountTypePermissionEntity atp ON p.id = atp.permissionId " +
            "WHERE atp.accountTypeId IN :accountTypeIds")
    List<PermissionEntity> findPermissionsByAccountTypeIds(@Param("accountTypeIds") List<Long> accountTypeIds);

    List<PermissionEntity> findByIdIn(List<Long> ids);

    @Query("SELECT p FROM PermissionEntity p " +
            "JOIN AccountTypePermissionEntity atp ON p.id = atp.permissionId " +
            "JOIN AccountTypeEntity at ON atp.accountTypeId = at.id " +
            "JOIN AccountEntity a ON a.accountTypeId = at.id " +
            "WHERE a.id = :accountId")
    List<PermissionEntity> getPermissionsByAccountId(@Param("accountId") Long accountId);

}
