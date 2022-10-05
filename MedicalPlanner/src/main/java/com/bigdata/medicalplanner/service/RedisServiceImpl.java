package com.bigdata.medicalplanner.service;

import com.bigdata.medicalplanner.repository.RedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisRepository<String> redisRepository;

    @Override
    public String getValue(String key) {
        logger.info("getValue ( key : " + key + " - Start");
        return redisRepository.getValue(key);
    }

    @Override
    public void postValue(String key, String value) {
        logger.info("postValue ( key : " + key + " value : " + value + " - Start");
        redisRepository.putValue(key, value);
        logger.info("postValue ( key : " + key + " value : " + value + " - End");
    }
}
