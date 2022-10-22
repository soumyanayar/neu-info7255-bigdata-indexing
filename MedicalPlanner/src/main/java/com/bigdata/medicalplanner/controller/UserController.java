package com.bigdata.medicalplanner.controller;
import com.bigdata.medicalplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value ="/plan/user/login")
    public String loginUser(String email, String password) {
        return userService.loginUser(email, password);
    }

    @PostMapping(value ="/user/register")
    public void registerUser(String email, String password, String firstName, String lastName) {
        userService.registerUser(email, password, firstName, lastName);
    }
}