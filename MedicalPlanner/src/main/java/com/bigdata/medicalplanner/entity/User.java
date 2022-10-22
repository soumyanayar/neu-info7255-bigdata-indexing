package com.bigdata.medicalplanner.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

//create user entity to store in redis
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public User() {
    }
}
