package com.bigdata.medicalplanner.entity;

import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

//create user entity to store in redis
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class User {
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public User() {
    }
}
