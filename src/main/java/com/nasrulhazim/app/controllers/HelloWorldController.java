package com.nasrulhazim.app.controllers;

import com.nasrulhazim.app.models.Hello;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping("/")
    public Hello helloWorld() {
        return new Hello("Nasrul");
    }
}
