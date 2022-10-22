package com.bigdata.medicalplanner.service;

import com.bigdata.medicalplanner.repository.UserRepository;
import com.bigdata.medicalplanner.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;

    private final SecurityUtils securityUtils;

    @Autowired
    public  UserService(UserRepository userRepository, SecurityUtils securityUtils) {
        this.userRepository = userRepository;
        this.securityUtils = securityUtils;
    }

    public String loginUser(String email, String password) {
        if(email == null || password == null) {
            return "Email or Password is not valid";
        }

       if(!userRepository.doesUserExist(email) || !isValidEmailAddress(email)) {
           return "Enter a valid email id";
       }

       String userEncryptedPassword = userRepository.getUser(email);
       if(securityUtils.isPassswordValid(password, userEncryptedPassword)) {
           return "Email or Password is not valid";
       }

       return "Logged in successfully";
    }

    public void registerUser(String email, String password, String firstName, String lastName) {
        if(userRepository.doesUserExist(email)) {
            throw new RuntimeException("User already exists");
        }
        String encryptedPassword = securityUtils.encodePassword(password);
        userRepository.addUser(email, encryptedPassword, firstName, lastName);
    }

    public boolean doesUserExist(String email) {
        return userRepository.doesUserExist(email);
    }

    public boolean deleteUser(String email) {
        if(!userRepository.doesUserExist(email)) {
            throw new RuntimeException("User does not exist");
        }
        return userRepository.deleteUser(email);
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
