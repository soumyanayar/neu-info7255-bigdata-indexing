package com.bigdata.medicalplanner.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
@EnableAutoConfiguration
public class RedisRepositoryImpl<T> implements RedisRepository<T> {
    private RedisTemplate<String, T> redisTemplate;
    private HashOperations<String, Object, T> hashOperation;
    private ValueOperations<String, T> valueOperations;

    @Autowired
    RedisRepositoryImpl(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperation = redisTemplate.opsForHash();
        this.valueOperations = redisTemplate.opsForValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void putValue(String key, T value) {
        valueOperations.set(key, (T) value);
        valueOperations.set(key + "_hash", (T) String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public T getValue(String key) {
        return valueOperations.get(key);
    }
}
