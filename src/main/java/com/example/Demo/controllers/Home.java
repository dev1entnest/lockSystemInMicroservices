package com.example.Demo.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/home")
@CrossOrigin(origins = "*")
@RestController
public class Home {

    @GetMapping("test")
    public String test(){
        return "Test Message";
    }
}
