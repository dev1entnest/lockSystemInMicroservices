package com.example.Demo.controllers;

import com.example.Demo.service.RedisLockService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api")
@CrossOrigin(origins = "*")
@RestController
public class MyController {

    private final RedisLockService redisLockService;

    public MyController(RedisLockService redisLockService) {
        this.redisLockService = redisLockService;
    }

    @GetMapping("lock")
    public String lock(){
        return redisLockService.lock();
    }

    @GetMapping("properlock")
    public String properLock(){
        return redisLockService.properLock();
    }

    @GetMapping("faillock")
    public String failLock(){
        redisLockService.failLock();
        return "Fail lock called output in the logs";
    }

    @GetMapping("simple")
    public String simple() {
        return redisLockService.simple();
    }
}
