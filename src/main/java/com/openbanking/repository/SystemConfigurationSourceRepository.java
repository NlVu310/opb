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
public interface SystemConfigurationSourceRepository extends BaseRepository<SystemConfigurationSourceEntity, Long> {
    @Query("SELECT scs.id as id , " +
            "scs.code as code, " +
            "scs.info as info, " +
            "scs.description as description, " +
            "scs.status as status, " +
            "p.name as partnerName " +
            "FROM SystemConfigurationSourceEntity scs " +
            "LEFT JOIN PartnerEntity p ON scs.partnerId = p.id " +
            "WHERE (:term IS NULL OR " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%')))")
    Page<SystemConfigurationSourceProjection> findByTerm(
            @Param("term") String term,
            Pageable pageable
    );

    @Query(value = "select s from SystemConfigurationSourceEntity s join PartnerEntity p on s.partnerId = p.id and s.partnerId = :id")
    List<SystemConfigurationSourceEntity> getListSourceByPartnerId(Long id);

    @Query(value = "select p.id from SystemConfigurationSourceEntity p where p.partnerId in :ids")
    List<Long> getListSourceIdByPartnerIds(@Param("ids") List<Long> ids);

    List<SystemConfigurationSourceEntity> findAllByCodeIn(List<String> codes);


}
