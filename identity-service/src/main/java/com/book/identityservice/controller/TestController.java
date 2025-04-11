package com.book.identityservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth/")
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Welcome to identity service";
    }
}
