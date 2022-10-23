package com.bigdata.medicalplanner.repository;


public interface RedisRepository<T> {
    public void putValue(String key, T value);

    public T getValue(String key);

    public boolean doesKeyExist(String key);

    public boolean deleteValue(String key);

}
