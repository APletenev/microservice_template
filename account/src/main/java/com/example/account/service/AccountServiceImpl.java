package com.example.account.service;

import com.example.account.entity.Account;
import com.example.account.repository.AccountRepository;
import com.example.common.coreapi.events.AccountCreatedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    @EventHandler
    @Override
    public void storeAccount(AccountCreatedEvent event) {
        accountRepository.save(new Account(event.getOwnerName(), event.getEmail(), 0));
    }


}
