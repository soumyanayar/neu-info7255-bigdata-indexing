package com.bigdata.medicalplanner.entity;

import lombok.Value;

@Value
public class JwtRequest {
    private String email;
    private String password;
}
