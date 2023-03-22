package com.example.userdetails.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserDetailsController {

    @GetMapping("/userdetails")
    public String getUserdetails() {
        return "balance, email";
    }
}
