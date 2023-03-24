package com.example.userdetails.repository;

import com.example.userdetails.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository <UserDetails, String> {
}
