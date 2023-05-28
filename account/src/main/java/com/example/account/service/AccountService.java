package com.example.account.service;

import com.example.common.coreapi.events.AccountCreatedEvent;

public interface AccountService {

    void storeAccount(AccountCreatedEvent event);
}
