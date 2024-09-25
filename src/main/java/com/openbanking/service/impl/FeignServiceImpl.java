package com.openbanking.service.impl;

import com.openbanking.entity.ReconciliationManageEntity;
import com.openbanking.entity.SystemConfigurationTransactionContentEntity;
import com.openbanking.entity.TransactionManageEntity;
import com.openbanking.enums.TransactionStatus;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundException;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionEnum;
import com.openbanking.model.bank_account.BankAccount;
import com.openbanking.model.reconciliation_manage.TestFeign;
import com.openbanking.model.transaction_manage.DebtClearance;
import com.openbanking.repository.ReconciliationManageRepository;
import com.openbanking.service.FeignService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FeignServiceImpl implements FeignService {

    @Autowired
    private ReconciliationManageRepository reconciliationManageRepository;

    @Override
    @Transactional
    public void handleClearanceReconciliation(List<DebtClearance> debtClearances) {
        try {
            if (debtClearances == null || debtClearances.isEmpty()) {
                return;
            }
            List<ReconciliationManageEntity> entities = new ArrayList<>();
            for (DebtClearance debtClearance : debtClearances) {
                ReconciliationManageEntity entity = new ReconciliationManageEntity();

                String dateStr = debtClearance.getTransDate();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyyHHmmss");
                OffsetDateTime transactionDate = OffsetDateTime.parse(dateStr, dateTimeFormatter.withZone(ZoneOffset.UTC));
                entity.setTransactionDate(transactionDate);
                entity.setDorc("C");
                entity.setTransactionId(debtClearance.getTransId());
                entity.setAmount(String.valueOf(debtClearance.getAmount()));
                entity.setReceiverAccountNo(debtClearance.getAccountNumber());
                entity.setReceiverAccount(debtClearance.getAccountName());
                entity.setContent(debtClearance.getDescription());
                entity.setSourceInstitution(debtClearance.getFrom());
                entity.setStatus(TransactionStatus.AWAITING_RECONCILIATION);

                entities.add(entity);
            }
            reconciliationManageRepository.saveAll(entities);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_TRANS, "" + e.getMessage());
        }
    }

    @Override
    public void handleClearanceReconciliation1(TestFeign testFeign) {

    }
}