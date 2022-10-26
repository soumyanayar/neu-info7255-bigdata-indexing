package com.bigdata.medicalplanner.repository;

import com.bigdata.medicalplanner.entity.User;

public interface UserRepository<T> {
    public User getUser(String email);
    public void addUser(User user);
    public boolean doesUserExist(String email);
    public boolean deleteUser(String email);
}
