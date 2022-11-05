package com.bigdata.medicalplanner.service;

import com.bigdata.medicalplanner.models.User;

public interface UserService {
    public void registerUser(User user);
    public User getUser(String username);
    public void deleteUser(String username);
    public boolean isUserExist(String username);
}
