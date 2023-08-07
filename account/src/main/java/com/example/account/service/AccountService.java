package com.example.account.service;

import com.example.account.entity.Account;

import java.util.UUID;

public interface AccountService {
    Account getDetailsByName(String username);

    void save(Account account);

    void deleteAccountById(UUID id);
}
