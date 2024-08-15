package com.openbanking.controller.cronjob;

import com.openbanking.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerStatusCronjob {
    private final BankAccountService bankAccountService;

    @PostConstruct
    public void init() {
        updateBankAccountStatuses();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateBankAccountStatuses() {
        try {
            log.info("Update bank account status");
            bankAccountService.updateBankAccountStatus();
        } catch (Exception e) {
            log.error("Update of bank account status failed", e);
        }
    }

}
