package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {

    @RequestMapping("/")
    public String home() {
        return "Server is work";
    }
}
