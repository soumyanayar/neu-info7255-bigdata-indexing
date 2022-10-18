package com.bigdata.medicalplanner.service;

import com.bigdata.medicalplanner.constants.CommonConstants;
import com.bigdata.medicalplanner.repository.RedisRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedisServiceTest {

    @Mock
    private RedisRepository<String> redisRepository;

    @Mock
    private ETagService eTagService;


    @BeforeEach
    void setUp()  {
    }

    @Test
    void when_keyExists_return_Object() throws JSONException {
        RedisService redisService = new RedisServiceImpl(eTagService, redisRepository);
        String key = "key";
        JSONObject value = new JSONObject();
        value.put("name", "Emily");
        when(redisRepository.getValue(key)).thenReturn(value.toString());
        JSONObject result = redisService.getValue(key);
        assertEquals(value.toString(), result.toString());
    }

    @Test
    void if_eTagExists_return_true() {
        RedisService redisService = new RedisServiceImpl(eTagService, redisRepository);
        String key = "key";
        String eTag = "eTag";
        when(redisRepository.getValue(CommonConstants.ETagKeyPrefix + key)).thenReturn(eTag);
        String result = redisService.getETagValue(key);
        assertEquals(eTag, result);
    }

    @Test
    void if_keyOrEtagExists_return_true() {
        RedisService redisService = new RedisServiceImpl(eTagService, redisRepository);
        String key = "key";
        when(redisRepository.doesKeyExist(key)).thenReturn(true);
        when(redisRepository.doesKeyExist(CommonConstants.ETagKeyPrefix + key)).thenReturn(true);
        boolean result = redisService.doesKeyExist(key);
        assertTrue(result);
    }

    @Test
    void when_postTheObject_then_returnEtag() throws JSONException {
        RedisService redisService = new RedisServiceImpl(eTagService, redisRepository);
        String key = "test";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Emily");
        jsonObject.put("age", 10);
        when(eTagService.computeETag(jsonObject)).thenReturn("testEtag");

        String eTag = redisService.postValue(key, jsonObject);
        assertNotNull(eTag);
        assertEquals("testEtag", eTag);
    }

    @Test
    void when_deleteById_Then_removeTheObject() {
        RedisService redisService = new RedisServiceImpl(eTagService, redisRepository);
        String key = "test";
        when(redisService.deleteValue(key)).thenReturn(true);
        when(redisService.deleteValue(CommonConstants.ETagKeyPrefix + key)).thenReturn(true);

        boolean isDeleted = redisService.deleteValue(key);
        assertTrue(isDeleted);
    }
}