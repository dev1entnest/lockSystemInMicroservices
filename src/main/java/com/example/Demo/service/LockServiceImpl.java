package com.example.Demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.ExpirableLockRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;

//@Component
public class LockServiceImpl {

    @Autowired
    private ExpirableLockRegistry lockRegistry;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public boolean update(String request) {
        Lock lock = lockRegistry.obtain(request);
        boolean success = lock.tryLock();
        if (!success) {
            return false;
        }
        logger.info("Acquired Lock and Proceeded With Lock");
        try {
            Thread.sleep(200);
        }catch (Exception e){
            e.printStackTrace();
        }

        // ...
        // update a shared resource
        // ...

        lock.unlock();
        return true;
    }
}
