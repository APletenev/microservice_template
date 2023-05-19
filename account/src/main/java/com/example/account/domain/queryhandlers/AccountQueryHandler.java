package com.example.account.domain.queryhandlers;

import com.example.account.entity.Account;
import com.example.account.repository.AccountRepository;
import com.example.common.coreapi.queries.AccountQuery;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AccountQueryHandler {

    private AccountRepository accountRepository;
    @QueryHandler
    public Account handle(AccountQuery query) {
        return accountRepository.findById(query.getUsername()).get() ;
    }
}
