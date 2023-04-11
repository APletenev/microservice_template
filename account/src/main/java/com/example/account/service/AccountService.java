package com.example.account.service;

import com.example.account.entity.Account;

public interface AccountService {
    Account getDetailsByName(String username);

    void save(Account account);
}
