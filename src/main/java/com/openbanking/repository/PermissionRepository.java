package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.PermissionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends BaseRepository<PermissionEntity, Long> {
    @Query("SELECT p FROM PermissionEntity p WHERE p.id IN (SELECT atp.permissionId FROM AccountTypePermissionEntity atp WHERE atp.accountTypeId = :accountTypeId)")
    List<PermissionEntity> findByAccountTypeId(@Param("accountTypeId") Long accountTypeId);
}
