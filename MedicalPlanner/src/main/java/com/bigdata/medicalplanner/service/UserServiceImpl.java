package com.bigdata.medicalplanner.service;

import com.bigdata.medicalplanner.models.JwtUserDetails;
import com.bigdata.medicalplanner.models.User;
import com.bigdata.medicalplanner.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final RedisRepository<User> redisRepository;

    @Autowired
    public UserServiceImpl(RedisRepository<User> redisRepository) {
        this.redisRepository = redisRepository;
    }

    @Override
    public void registerUser(User user) {
        redisRepository.putValue(user.getUsername(), user);
    }

    @Override
    public User getUser(String username) {
        return redisRepository.getValue(username);
    }

    @Override
    public void deleteUser(String username) {
        redisRepository.deleteValue(username);
    }

    @Override
    public boolean isUserExist(String username) {
        return redisRepository.doesKeyExist(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = redisRepository.getValue(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new JwtUserDetails(user);
    }
}
