package com.example.userdetails.service;

import com.example.userdetails.entity.UserDetails;
import com.example.userdetails.repository.UserDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements com.example.userdetails.service.UserDetailsService {

    private UserDetailsRepository userDetailsRepository;

    @Override
    public UserDetails getDetailsByName(String username) {
        return userDetailsRepository.findById(username).get() ;
    }

    @Override
    public void save(UserDetails userDetails) {
        userDetailsRepository.save(userDetails);
    }


}
