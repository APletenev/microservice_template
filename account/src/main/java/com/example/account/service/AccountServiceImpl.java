package com.example.account.service;

import com.example.account.entity.Account;
import com.example.account.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    @Override
    public Account getDetailsByName(String username) {
        return accountRepository.getAccountByUsername(username).orElse(null);
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }

    public void deleteAccountById(UUID id) {
        accountRepository.deleteById(id);
    }


}
