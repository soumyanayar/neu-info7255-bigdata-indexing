package com.bigdata.medicalplanner.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
@EnableAutoConfiguration
public class RedisRepositoryImpl<T> implements RedisRepository<T> {
    private final RedisTemplate<String, T> redisTemplate;
    private final ValueOperations<String, T> valueOperations;

    @Autowired
    RedisRepositoryImpl(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    @Override
    public void putValue(String key, T value) {
        valueOperations.set(key, (T) value);
    }

    @Override
    public T getValue(String key) {
        return valueOperations.get(key);
    }

    @Override
    public boolean deleteValue(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }
}
