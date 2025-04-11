package com.book.bookservice.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")

public class TestController {
    @GetMapping("/test")
    public String getBooks() {
        return "Hello from book-service!";
    }
}
