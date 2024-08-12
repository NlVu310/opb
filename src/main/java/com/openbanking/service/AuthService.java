package com.openbanking.service;


import com.openbanking.comon.BaseService;
import com.openbanking.model.account.Account;
import com.openbanking.model.account.CreateAccount;
import com.openbanking.model.account.UpdateAccount;
import com.openbanking.model.login.LoginRQ;
import com.openbanking.model.login.LoginRS;
import com.openbanking.model.login.RegisterRQ;
import org.springframework.stereotype.Service;

@Service
public interface AuthService extends BaseService<Account, CreateAccount, UpdateAccount, Long> {
    LoginRS login(LoginRQ rq);
    Account register(RegisterRQ rq);
}
