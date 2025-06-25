package com.book.notificationservice.service.impl;

import com.book.notificationservice.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailServiceImpl implements EmailService {


    @Override
    public void send(String recipient, String subject, String body) {
        log.info("[EMAIL SERVICE] Sending email to: {},\n Subject: {}, \nBody: {}", recipient, subject, body);
    }
}
