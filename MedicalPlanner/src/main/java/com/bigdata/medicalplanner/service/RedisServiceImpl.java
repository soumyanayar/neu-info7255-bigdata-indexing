package com.bigdata.medicalplanner.service;

import com.bigdata.medicalplanner.constants.CommonConstants;
import com.bigdata.medicalplanner.repository.RedisRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {
    private final Logger logger;

    private final ETagService eTagService;
    private final RedisRepository<String> redisRepository;

    @Autowired
    public RedisServiceImpl(ETagService eTagService, RedisRepository<String> redisRepository) {
        this.eTagService = eTagService;
        this.redisRepository = redisRepository;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public JSONObject getValue(String key) {
        logger.info("getValue ( key : " + key + " - Start");
        String value = redisRepository.getValue(key);
        if (value == null) {
            return null;
        }

        return new JSONObject(value);
    }

    @Override
    public String getETagValue(String key) {
        return redisRepository.getValue(CommonConstants.ETagKeyPrefix + key);
    }

    @Override
    public boolean doesKeyExist(String key) {
        return redisRepository.doesKeyExist(key) && redisRepository.doesKeyExist(CommonConstants.ETagKeyPrefix + key);
    }

    @Override
    public String postValue(String key, JSONObject value) {
        logger.info("postValue ( key : " + key + " value : " + value + " - Start");
        redisRepository.putValue(key, value.toString());
        String eTag = eTagService.computeETag(value);
        redisRepository.putValue(CommonConstants.ETagKeyPrefix + key, eTag);
        logger.info("postValue ( key : " + key + " value : " + value + " - End");
        return eTag;
    }

    @Override
    public boolean deleteValue(String key) {
        logger.info("deleteValue ( key : " + key + " - Start");
        boolean isDeleted = redisRepository.deleteValue(key) && redisRepository.deleteValue(CommonConstants.ETagKeyPrefix + key);
        logger.info("deleteValue ( key : " + key + " - End");
        return isDeleted;
    }
}
