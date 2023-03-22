package com.example.userdetails.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "userdetails")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDetails {
    @Id
    private String username;
    @Email
    private String email;
    @Min(0)
    private long balance;

}
