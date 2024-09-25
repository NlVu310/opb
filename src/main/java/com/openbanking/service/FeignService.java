package com.openbanking.service;

import com.openbanking.model.reconciliation_manage.TestFeign;
import com.openbanking.model.transaction_manage.DebtClearance;
import feign.Feign;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;


@Service
@FeignClient(name = "apec-client", url = "https://192.168.64.69:6443")
public interface FeignService {
    @PostMapping("/apec/api/epay/order/create")
    void handleClearanceReconciliation(@RequestBody List<DebtClearance> debtClearances);

    @PostMapping("/apec/api/transaction/balance-fluctuation")
    void handleClearanceReconciliation1(@RequestBody TestFeign testFeign);
}

