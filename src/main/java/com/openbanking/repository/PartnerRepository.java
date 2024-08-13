package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.PartnerEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository  extends BaseRepository<PartnerEntity, Long> {
}
