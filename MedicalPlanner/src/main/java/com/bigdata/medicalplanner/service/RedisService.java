package com.bigdata.medicalplanner.service;

import org.json.JSONObject;

public interface RedisService {
    public JSONObject getValue(final String key);

    public String getETagValue(String key);

    public boolean doesKeyExist(String key);

    public String postValue(final String key, final JSONObject value);

    public boolean deleteValue(final String key);

}
