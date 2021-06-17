package com.sda_store.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping(path = "/test")
    public HttpStatus testAuth() {
        return HttpStatus.OK;
    }

    @GetMapping(path = "/hello-world")
    public String HelloWorldString() {
        return "Hello World! This is the SDA test controller!";
    }
}
