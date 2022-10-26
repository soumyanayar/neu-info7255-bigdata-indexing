package com.bigdata.medicalplanner.repository;

import com.bigdata.medicalplanner.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
@EnableAutoConfiguration
public class UserRepositoryImpl<T> implements UserRepository<T>{
    private final RedisTemplate<String, T> redisTemplate;
    private final ValueOperations<String, T> valueOperations;

   // private static final String KEY = "USER";

    @Autowired
    UserRepositoryImpl(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    @Override
    public User getUser(String email) {
        User user = (User) valueOperations.get(email);
        return user;
    }

    @Override
    public void addUser(User user) {
        try{
            valueOperations.set(user.getEmail(), (T) user);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean doesUserExist(String email) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(email));
    }

    @Override
    public boolean deleteUser(String email) {
        return Boolean.TRUE.equals(redisTemplate.delete(email));
    }
}
