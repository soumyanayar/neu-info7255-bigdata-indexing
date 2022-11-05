package com.bigdata.medicalplanner.models;

import lombok.Value;

import java.security.PrivateKey;
import java.security.PublicKey;

@Value
public class JwtKeys {
    PublicKey publicKey;
    PrivateKey privateKey;
}
