package com.bigdata.medicalplanner.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import java.io.Serializable;

//create user entity to store in redis
@Builder
@Data
public class User implements Serializable {
    @Id
    String email;
    String password;
    String firstName;
    String lastName;
}
