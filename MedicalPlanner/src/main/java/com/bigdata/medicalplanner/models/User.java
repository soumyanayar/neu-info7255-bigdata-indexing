package com.bigdata.medicalplanner.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import java.io.Serializable;

//create user entity to store in redis
@Builder
@Data
public class User implements Serializable {
    @Id
    String username;
    String password;
    String firstName;
    String lastName;
    boolean active;
    String roles;
}
