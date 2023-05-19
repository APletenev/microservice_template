package com.example.account.controller;

import com.example.account.entity.Account;
import com.example.account.service.AccountService;
import com.example.common.Roles;
import com.example.common.coreapi.queries.AccountQuery;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("#{environment.ACCOUNT_API}")
@RestController
@AllArgsConstructor
public class AccountController {
    private QueryGateway queryGateway;

    /**
     * Админ может просматривать информацию о пользователях
     * @param username имя пользователя, информацию по которому нужно посмотреть
     * @return email пользователя и его текущий баланс
     */
    @PreAuthorize("hasRole('" + Roles.ADMIN_ROLE + "')")
    @GetMapping("/{username}")
    public Account getAccount(@PathVariable String username) {

        return queryGateway.query(new AccountQuery(username), Account.class).join();
    }
}
