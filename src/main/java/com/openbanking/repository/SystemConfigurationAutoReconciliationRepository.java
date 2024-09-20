package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.SystemConfigurationAutoReconciliationEntity;
import com.openbanking.model.system_configuration_auto_reconciliation.SystemConfigurationAutoReconciliationProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemConfigurationAutoReconciliationRepository extends BaseRepository<SystemConfigurationAutoReconciliationEntity, Long> {
    @Query("SELECT r.id as id, s.id as sourceId, s.code as sourceCode, r.reconciliationTime as reconciliationTime," +
            " r.reconciliationFrequencyNumber as reconciliationFrequencyNumber, " +
            "r.reconciliationFrequencyUnit as reconciliationFrequencyUnit, " +
            "r.retryTimeNumber as retryTimeNumber, r.retryFrequencyNumber as retryFrequencyNumber " +
            "FROM SystemConfigurationSourceEntity s " +
            "LEFT JOIN SystemConfigurationAutoReconciliationEntity r " +
            "ON s.id = r.sourceId " +
            "WHERE s.partnerId = :partnerId")
    List<SystemConfigurationAutoReconciliationProjection> getListByPartnerId(@Param("partnerId") Long partnerId);

    Optional<SystemConfigurationAutoReconciliationEntity> findFirstBySourceCode(String sourceCode);

}
