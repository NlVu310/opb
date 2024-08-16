package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.PartnerEntity;
import com.openbanking.model.partner.CreatePartner;
import com.openbanking.model.partner.Partner;
import com.openbanking.model.partner.PartnerDetail;
import com.openbanking.model.partner.UpdatePartner;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
public interface PartnerMapper extends BaseMapper<PartnerEntity, Partner, CreatePartner, UpdatePartner> {
    PartnerDetail getDetail(PartnerEntity partnerEntity);
}
