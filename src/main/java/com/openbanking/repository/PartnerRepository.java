package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.PartnerEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository  extends BaseRepository<PartnerEntity, Long> {
    @Query(value = "select p.id from PartnerEntity p " +
            "join SystemConfigurationSourceEntity s on p.id = s.partnerId " +
            "join SystemConfigurationAutoReconciliationEntity r on s.id = r.sourceId " +
            "where r.id = :id")
    Long getPartnerNameByReconciliationId(Long id);
}
