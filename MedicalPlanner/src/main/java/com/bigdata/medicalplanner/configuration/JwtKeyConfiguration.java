package com.bigdata.medicalplanner.configuration;

import com.bigdata.medicalplanner.models.JwtKeys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.*;
import java.util.Base64;

@Configuration
public class JwtKeyConfiguration {
    @Bean
    public JwtKeys getKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        System.out.println("Public key content: " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        PrivateKey privateKey = keyPair.getPrivate();
        return new JwtKeys(publicKey, privateKey);
    }
}
