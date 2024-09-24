package com.openbanking.controller.cronjob;

import com.openbanking.service.ReconciliationManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Component
@Slf4j
@RequiredArgsConstructor
public class ReconciliationCronjob {

    private final ReconciliationManageService reconciliationService;

    @PostConstruct
    public void init() {
        performReconciliation();
    }

    @Scheduled(fixedRate = 60000)
    public void performReconciliation() {
        try {
            reconciliationService.performReconciliation();
        } catch (Exception e) {
            log.error("Scheduled reconciliation task failed", e);
        }
    }
}