package com.example.Demo.service;

public interface RedisLockService {

    String lock();
    void failLock();
    String properLock();

    String simple();
}
