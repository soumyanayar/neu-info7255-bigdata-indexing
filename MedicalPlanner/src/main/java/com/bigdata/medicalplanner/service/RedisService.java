package com.bigdata.medicalplanner.service;

public interface RedisService {
    public String getValue(final String key);

    public void postValue(final String key, final String value);

    public boolean deleteValue(final String key);
}
