package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.JobSchedulerEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobSchedulerRepository extends BaseRepository<JobSchedulerEntity, Long> {
    @Query(value = "SELECT * FROM job_scheduler WHERE ref_id = :refId ORDER BY id DESC LIMIT 1", nativeQuery = true)
    JobSchedulerEntity findFirstByRefId(@Param("refId") Long refId);

}
