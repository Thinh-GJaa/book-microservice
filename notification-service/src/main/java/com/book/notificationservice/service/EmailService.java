package com.book.notificationservice.service;


public interface EmailService {

    void send(String recipient, String subject, String body);
}
