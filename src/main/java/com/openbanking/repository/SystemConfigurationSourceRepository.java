package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.SystemConfigurationSourceEntity;
import com.openbanking.model.system_configuration_source.SystemConfigurationSourceProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SystemConfigurationSourceRepository  extends BaseRepository<SystemConfigurationSourceEntity, Long> {
    @Query("SELECT scs.id as id , " +
            "scs.code as code, " +
            "scs.info as info, " +
            "scs.description as description, " +
            "scs.status as status, " +
            "p.name as partnerName " +
            "FROM SystemConfigurationSourceEntity scs " +
            "LEFT JOIN PartnerEntity p ON scs.partnerId = p.id " +
            "WHERE (:term IS NULL OR " +
            "LOWER(scs.code) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(scs.info) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(scs.description) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(scs.status) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%')))")
    Page<SystemConfigurationSourceProjection> findByTerm(
            @Param("term") String term,
            Pageable pageable
    );
}
