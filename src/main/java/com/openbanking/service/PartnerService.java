package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.model.partner.CreatePartner;
import com.openbanking.model.partner.Partner;
import com.openbanking.model.partner.UpdatePartner;
import org.springframework.stereotype.Service;

@Service
public interface PartnerService extends BaseService<Partner, CreatePartner, UpdatePartner, Long> {

}
