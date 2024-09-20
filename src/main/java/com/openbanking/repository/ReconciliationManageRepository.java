package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.ReconciliationManageEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReconciliationManageRepository extends BaseRepository<ReconciliationManageEntity, Long> {
    @Query("SELECT r FROM ReconciliationManageEntity r WHERE r.sourceInstitution = :sourceCode")
    List<ReconciliationManageEntity> findBySource(@Param("sourceCode") String sourceCode);
}
