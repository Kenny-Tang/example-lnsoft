package com.redjujubetree.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping("/hello" )
    public String hello() {
        System.out.println("enter the hello method");
        return "hello world";
    }
}
