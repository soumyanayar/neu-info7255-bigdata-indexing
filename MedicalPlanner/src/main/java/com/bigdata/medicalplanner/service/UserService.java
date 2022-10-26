package com.bigdata.medicalplanner.service;

import com.bigdata.medicalplanner.entity.User;
import com.bigdata.medicalplanner.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserService  {
    private final UserRepository userRepository;

    @Autowired
    public  UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public JSONObject getUser(String email) {
        User user = (User) userRepository.getUser(email);
        if(user == null) {
            return null;
        }
        JSONObject json = new JSONObject();
        json.put("email", user.getEmail());
        json.put("password", user.getPassword());
        json.put("firstName", user.getFirstName());
        json.put("lastName", user.getLastName());
        return json;
    }

    public void registerUser(User user) {
        //String encryptedPassword = securityUtils.encodePassword(password);
        userRepository.addUser(user);
    }

    public boolean deleteUser(String email) {
        if(!userRepository.doesUserExist(email)) {
            throw new RuntimeException("User does not exist");
        }
        return userRepository.deleteUser(email);
    }

    public boolean doesUserExist(String email) {
        return userRepository.doesUserExist(email);
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

}
