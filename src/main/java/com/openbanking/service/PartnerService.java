package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.comon.PaginationRS;
import com.openbanking.model.customer.CreateCustomer;
import com.openbanking.model.customer.UpdateCustomer;
import com.openbanking.model.partner.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PartnerService extends BaseService<Partner, CreatePartner, UpdatePartner, Long> {
    PartnerDetail getDetailById(Long id);
    void create(CreatePartner createPartner);
    void update(UpdatePartner updatePartner);

    PaginationRS<Partner> getListPartnerByAccount(Long accountId, SearchPartnerRQ searchRQ);
    void deleteByListId(List<Long> ids);


}
