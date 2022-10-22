package com.bigdata.medicalplanner.repository;

public interface UserRepository {
    public String getUser(String email);
    public void addUser(String email, String password, String firstName, String lastName);
    public boolean doesUserExist(String email);
    public boolean deleteUser(String email);
}
