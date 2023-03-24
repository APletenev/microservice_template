package com.example.userdetails.controller;

import com.example.userdetails.entity.UserDetails;
import com.example.userdetails.service.UserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/userdetails")
@RestController
@AllArgsConstructor
public class UserDetailsController {
    private UserDetailsService userDetailService;

    /**
     * Админ может просматривать информацию о пользователях
     * @param username имя пользователя, информацию по которому нужно посмотреть
     * @return email пользователя и его текущий баланс
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{username}")
    public UserDetails getUserdetails(@PathVariable String username) {
        return userDetailService.getDetailsByName(username);
    }
}
