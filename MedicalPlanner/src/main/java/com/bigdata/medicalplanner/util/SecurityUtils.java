package com.bigdata.medicalplanner.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtils {
    //Encode password using RSA
    public String encodePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String result = encoder.encode(password);
        return result;
    }

    //Check if password matches
    public boolean isPassswordValid(String password, String encodedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        return encoder.matches(password, encodedPassword);
    }


}
