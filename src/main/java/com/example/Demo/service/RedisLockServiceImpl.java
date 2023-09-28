package com.example.Demo.service;

import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Service
public class RedisLockServiceImpl implements RedisLockService {

    private static final String MY_LOCK_KEY = "SomeLockKey";
    private final LockRegistry lockRegistry;



    public RedisLockServiceImpl(LockRegistry lockRegistry) {
        this.lockRegistry = lockRegistry;
    }

    @Override
    public String lock() {
        String threadName=" MY REQUEST THREAD " + Thread.currentThread().getName()+" ";
        Lock lock = lockRegistry.obtain(MY_LOCK_KEY);
        boolean success = lock.tryLock();
        String returnVal = null;
        if (!success){
            returnVal = threadName+"Lock not acquired";
        }else {
            System.out.println(threadName+"Performing some operations!!");
            returnVal = threadName+"Lock retrieve successfully!";
            try {
                System.out.println(threadName+"Thread Sleep for 5 sec");
                Thread.sleep(5000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        lock.unlock();
        return returnVal;
    }

    @Override
    public void failLock() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Runnable lockThreadOne = () -> {
            String threadName=" MY REQUEST THREAD " + Thread.currentThread().getName()+" ";
            UUID uuid = UUID.randomUUID();
            StringBuilder sb = new StringBuilder();
            Lock lock = lockRegistry.obtain(MY_LOCK_KEY);
            try {
                System.out.println(threadName+"Attempting to lock the thread"+ uuid);
                if (lock.tryLock()){
                    System.out.println(threadName+"Locked with thread "+ uuid);
                    Thread.sleep(5000);
                }else {
                    System.out.println(threadName+"Failed to lock with thread "+uuid);

                }
            }catch (Exception e){
                System.out.println(threadName+"Exception with thread "+ uuid+" "+e.getMessage());
            }finally {
                lock.unlock();
                System.out.println(String.format(threadName+"Unlock the thread in fallback in first thread %s", uuid));
            }
        };

        Runnable lockThreadTwo = () -> {
            String threadName=" MY REQUEST THREAD " + Thread.currentThread().getName()+" ";
            try {
                System.out.println(threadName+"inside second thread");
                Thread.sleep(5000);
            }catch (Exception e){
                System.out.println(threadName+"Exception in second thread");
            }
        };
        executor.submit(lockThreadOne);
        executor.submit(lockThreadTwo);
        executor.shutdown();
    }

    @Override
    public String properLock() {
        String threadName=" MY REQUEST THREAD " + Thread.currentThread().getName()+" ";
        Lock lock=null;
        try {
            lock=lockRegistry.obtain(MY_LOCK_KEY);
        }catch (Exception e){
            System.out.println(String.format(threadName+"Unable to obtain the lock: %s", MY_LOCK_KEY));
            System.out.println(String.format(threadName+"Here is error message: %s"+e.getMessage()));
        }
        String returnVal = null;
        try {
            returnVal = threadName+"Lock acquired successfully!";
            if (lock.tryLock()){
                System.out.println(threadName+"Performing Some Operations in Production");
                try {
                    System.out.println(threadName+"Thread Sleep for 5 sec");
                    Thread.sleep(5000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println(threadName+"Operation ended in production");
            }else {
                System.out.println(threadName+"Lock not acquired in production");
                returnVal = threadName+"Lock not acquired in production";
            }
        }catch (Exception e){
            //in a production environment this should log and do something else.
            returnVal = threadName+"Exception in Getting production";
            System.out.println(threadName+"Exception in Getting production lock!!");
        } finally {
            //always have this finally block if anything goes wrong
            try {
                lock.unlock();
            }catch (Exception e){
                System.out.println(threadName+"Locked released from Finally Block in prod lock");
            }
        }
        return returnVal;
    }

    @Override
    public String simple() {
        String threadName=" MY REQUEST THREAD " + Thread.currentThread().getName()+" ";
        try {
            Thread.sleep(5000);
            System.out.println(threadName+" SIMPLE METHOD Sleep for 5  Seconds");
        }catch (Exception e){
            System.out.println(threadName+" Exception in SIMPLE METHOD");
        }finally {
            System.out.println(threadName+" FINAL BLOCK OF SIMPLE METHOD");
        }
        return "Simple String Message";
    }
}
