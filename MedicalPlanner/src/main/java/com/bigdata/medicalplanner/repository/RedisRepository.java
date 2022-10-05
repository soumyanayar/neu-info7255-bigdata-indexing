package com.bigdata.medicalplanner.repository;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface RedisRepository<T> {
    public void putValue(String key, T value);

    public T getValue(String key);

    public boolean deleteValue(String key);
}
