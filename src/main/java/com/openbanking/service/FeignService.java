package com.openbanking.service;


import com.openbanking.model.reconciliation_manage.FeignResult;
import com.openbanking.model.reconciliation_manage.TestFeign;
import com.openbanking.model.transaction_manage.DebtClearance;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;
import java.util.Map;


@Service
@FeignClient(name = "apec-client", url = "https://192.168.64.69:6443")
public interface FeignService {
    @PostMapping("/apec/api/epay/order/create")
    void handleClearanceReconciliation(@RequestBody List<DebtClearance> debtClearances);

    @PostMapping(value = "/apec/api/transaction/balance-fluctuation", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResult handleClearanceReconciliation1(
            @RequestBody TestFeign testFeign,
            @RequestHeader Map<String, String> headers
    );
}



