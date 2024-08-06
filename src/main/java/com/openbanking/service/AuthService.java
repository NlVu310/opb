package com.openbanking.service;


import com.openbanking.model.login.LoginRQ;
import com.openbanking.model.login.LoginRS;

public interface AuthService {
    LoginRS login(LoginRQ rq);
}
