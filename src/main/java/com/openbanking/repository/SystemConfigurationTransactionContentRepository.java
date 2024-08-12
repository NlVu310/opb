package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.SystemConfigurationTransactionContentEntity;
import com.openbanking.model.system_configuration_transaction_content.SystemConfigurationTransactionContentProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemConfigurationTransactionContentRepository extends BaseRepository<SystemConfigurationTransactionContentEntity, Long> {
    @Query("SELECT sctc.id as id, sctc.customerId AS customerId, " +
            "sctc.source AS source, " +
            "sctc.refNoLength AS refNoLength, " +
            "sctc.refNoStart AS refNoStart, " +
            "sctc.refNoEnd AS refNoEnd, " +
            "sctc.accountId AS accountId, " +
            "c.name AS customerName " +
            "FROM SystemConfigurationTransactionContentEntity sctc " +
            "LEFT JOIN CustomerEntity c ON sctc.customerId = c.id " +
            "WHERE (:term IS NULL OR " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "CAST(sctc.source AS string) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "CAST(sctc.refNoLength AS string) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "CAST(sctc.refNoStart AS string) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "CAST(sctc.refNoEnd AS string) LIKE LOWER(CONCAT('%', :term, '%')))")
    Page<SystemConfigurationTransactionContentProjection> findByTerm(
            @Param("term") String term,
            Pageable pageable
    );
}


