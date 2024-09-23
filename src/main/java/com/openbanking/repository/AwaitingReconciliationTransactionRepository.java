package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.AwaitingReconciliationTransactionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AwaitingReconciliationTransactionRepository extends BaseRepository<AwaitingReconciliationTransactionEntity, Long> {
    @Query("SELECT aw FROM AwaitingReconciliationTransactionEntity aw WHERE aw.sourceInstitution = :sourceCode")
    List<AwaitingReconciliationTransactionEntity> findBySource(@Param("sourceCode") String sourceCode);
    Optional<AwaitingReconciliationTransactionEntity> findBySourceInstitutionAndTransactionId(String sourceInstitution, String transactionId);
    List<AwaitingReconciliationTransactionEntity> findBySourceInstitutionInAndTransactionDateBetween(List<String> sources, OffsetDateTime from, OffsetDateTime to);

}