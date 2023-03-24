package com.example.userdetails.service;

import com.example.userdetails.entity.UserDetails;

public interface UserDetailsService {
    UserDetails getDetailsByName(String username);
}
