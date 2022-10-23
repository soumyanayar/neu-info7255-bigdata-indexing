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

    @Autowired
    UserRepositoryImpl(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    @Override
    public String getUser(String email) {
        valueOperations.get(email);
        return valueOperations.get(email).toString();
    }

    @Override
    public void addUser(String email, String password, String firstName, String lastName) {
        User user = User.builder()
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .build();
        valueOperations.set(email, (T) user);
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
