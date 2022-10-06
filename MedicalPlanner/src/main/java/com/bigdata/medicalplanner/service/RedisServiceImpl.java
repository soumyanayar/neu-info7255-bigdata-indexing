package com.bigdata.medicalplanner.service;

import com.bigdata.medicalplanner.repository.RedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {
    private final Logger logger;
    private final RedisRepository<String> redisRepository;

    @Autowired
    public RedisServiceImpl(RedisRepository<String> redisRepository) {
        this.redisRepository = redisRepository;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

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

    @Override
    public boolean deleteValue(String key) {
        logger.info("deleteValue ( key : " + key + " - Start");
        boolean isDeleted = redisRepository.deleteValue(key);
        logger.info("deleteValue ( key : " + key + " - End");
        return isDeleted;
    }
}
