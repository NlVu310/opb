package com.openbanking.service;


import com.openbanking.comon.BaseService;
import com.openbanking.entity.AccountEntity;
import com.openbanking.model.Account;
import com.openbanking.model.login.LoginRQ;
import com.openbanking.model.login.LoginRS;
import com.openbanking.model.login.RegisterRQ;
import org.springframework.stereotype.Service;

@Service
public interface AuthService extends BaseService<Account, Long> {
    LoginRS login(LoginRQ rq);
    Account register(RegisterRQ rq);
}
