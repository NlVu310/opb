package com.openbanking.controller.cronjob;

import com.openbanking.service.ReconciliationManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReconciliationCronjob {

    private final ReconciliationManageService reconciliationService;
    private final Lock lock = new ReentrantLock();

    @PostConstruct
    public void init() {
        performReconciliation();
    }

    @Scheduled(fixedRate = 60000)
    public void performReconciliation() {
        if (lock.tryLock()) {
            try {
                reconciliationService.runReconciliationJobs();
            } catch (Exception e) {
                log.error("Scheduled reconciliation task failed", e);
            } finally {
                lock.unlock();
            }
        } else {
            log.info("Job is already running, skipping this execution.");
        }
    }
}
