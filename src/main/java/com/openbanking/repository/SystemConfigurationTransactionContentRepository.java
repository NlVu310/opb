package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.SystemConfigurationTransactionContentEntity;
import com.openbanking.model.system_configuration_transaction_content.SystemConfigurationTransactionContentProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemConfigurationTransactionContentRepository extends BaseRepository<SystemConfigurationTransactionContentEntity, Long> {
    @Query("SELECT sctc.id AS id, sctc.customerId AS customerId, " +
            "sctc.sourceStart AS sourceStart, " +
            "sctc.sourceLengthEnd AS sourceLengthEnd, " +
            "sctc.sourceIndexEnd AS sourceIndexEnd, " +
            "sctc.sourceRegex AS sourceRegex, " +
            "sctc.refNoStart AS refNoStart, " +
            "sctc.refNoLengthEnd AS refNoLengthEnd, " +
            "sctc.refNoIndexEnd AS refNoIndexEnd, " +
            "sctc.refNoRegex AS refNoRegex, " +
            "c.name AS customerName " +
            "FROM SystemConfigurationTransactionContentEntity sctc " +
            "LEFT JOIN CustomerEntity c ON sctc.customerId = c.id " +
            "WHERE (:term IS NULL OR " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(sctc.sourceStart) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "CAST(sctc.sourceLengthEnd AS string) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(sctc.sourceIndexEnd) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(sctc.sourceRegex) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(sctc.refNoStart) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "CAST(sctc.refNoLengthEnd AS string) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(sctc.refNoIndexEnd) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(sctc.refNoRegex) LIKE LOWER(CONCAT('%', :term, '%'))) ")
    Page<SystemConfigurationTransactionContentProjection> findByTerm(
            @Param("term") String term,
            Pageable pageable
    );


    @Query(value = "select s.id from SystemConfigurationTransactionContentEntity s where s.customerId in :ids")
    List<Long> getListTransactionContentIdByCustomerIds(@Param("ids") List<Long> ids);

    void deleteByCustomerIdIn(List<Long> customerIds);
    
    @Query("select s.id from SystemConfigurationTransactionContentEntity s ")
    List<Long> getListCustomerId ();
}


