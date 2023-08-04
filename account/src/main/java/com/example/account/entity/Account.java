package com.example.account.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "account")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Account implements Persistable<String> {
    @Id
    private String username;
    @Email
    private String email;
    @Min(0)
    private long balance;
    @Transient
    private boolean update;
    @Override
    public String getId() {
        return username;
    }

    @Override
    public boolean isNew() {
        return !update;
    }
    @PostLoad
    void markUpdated() {
        this.update = true;
    }
    @PreRemove
    void markNew() {
        this.update = false;
    }

}
