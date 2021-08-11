package com.javaproj.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
    @GetMapping("/test")
    public String helloForWeb() {
        return "Hello world";
    }
}
