package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.comon.PaginationRS;
import com.openbanking.model.partner.*;
import org.springframework.stereotype.Service;

@Service
public interface PartnerService extends BaseService<Partner, CreatePartner, UpdatePartner, Long> {
    PartnerDetail getDetailById(Long id);
    PaginationRS<Partner> getListPartner(SearchPartnerRQ searchRQ);

}
