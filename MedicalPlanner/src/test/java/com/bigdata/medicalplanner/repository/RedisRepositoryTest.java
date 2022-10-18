package com.bigdata.medicalplanner.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RedisRepositoryTest {

    @Mock
    private ValueOperations valueOperations;

    @Mock
    private RedisTemplate redisTemplate;

    @Test
    void putValue_doesKeyExist_success() {
        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        RedisRepositoryImpl redisRepository = new RedisRepositoryImpl(redisTemplate);
        String key = "key";
        String value = "value";
        Mockito.doNothing().when(valueOperations).set(key, value);
        Mockito.when(redisTemplate.hasKey(key)).thenReturn(true);
        Mockito.when(valueOperations.get(key)).thenReturn(value);
        redisRepository.putValue(key, value);
        Assertions.assertTrue(redisRepository.doesKeyExist(key));
        Assertions.assertEquals(redisRepository.getValue(key), value);
    }

    @Test
    void getValue_success() {
        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        RedisRepositoryImpl redisRepository = new RedisRepositoryImpl(redisTemplate);
        String key = "key";
        String value = "value";
        Mockito.when(valueOperations.get(key)).thenReturn(value);
        Assertions.assertEquals(redisRepository.getValue(key), value);
    }


    @Test
    void deleteValue() {
        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        RedisRepositoryImpl redisRepository = new RedisRepositoryImpl(redisTemplate);
        String key = "key";
        String value = "value";
        Mockito.when(redisTemplate.delete(key)).thenReturn(true);
        Assertions.assertTrue(redisRepository.deleteValue(key));
    }
}