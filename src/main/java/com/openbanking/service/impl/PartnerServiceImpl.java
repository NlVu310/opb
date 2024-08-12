package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.PartnerEntity;
import com.openbanking.model.partner.CreatePartner;
import com.openbanking.model.partner.Partner;
import com.openbanking.model.partner.UpdatePartner;
import com.openbanking.service.PartnerService;
import org.springframework.stereotype.Service;

@Service
public class PartnerServiceImpl extends BaseServiceImpl<PartnerEntity, Partner, CreatePartner, UpdatePartner, Long> implements PartnerService {
    public PartnerServiceImpl(BaseRepository<PartnerEntity, Long> repository, BaseMapper<PartnerEntity, Partner, CreatePartner, UpdatePartner> mapper) {
        super(repository, mapper);
    }
}
