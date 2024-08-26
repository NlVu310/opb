package com.openbanking.model.customer;

import com.openbanking.comon.BaseDTO;
import com.openbanking.model.transaction_manage.TransactionManage;
import lombok.Data;

import java.util.List;

@Data
public class CustomerTransactionDetail{
    private List<TransactionManage> transactionManageList;
}
