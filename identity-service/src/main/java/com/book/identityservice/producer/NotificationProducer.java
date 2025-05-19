package com.book.identityservice.producer;

import com.book.identityservice.dto.event.NotificationEvent;
import com.book.identityservice.parser.Parser;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationProducer {

    KafkaTemplate<String, String> kafkaTemplate;

    Parser parser;

    public void createProfileNotification(String userName, String email){
        NotificationEvent event = NotificationEvent.builder()
                .channel("Email")
                .recipient(email)
                .subject("Welcome to microservice (ThinhGJaa)")
                .body("Hello, " + userName)
                .build();

        kafkaTemplate.send("create-profile-topic", parser.parseToJson(event));
    }

    public void resetLinkNotification(String email, String resetLink){

        String content = """
            Hello ,

            We received a request to reset the password for your account. If you did not make this request, please ignore this email.

            To reset your password, please click the link below:

            👉 %s

            This link will expire in 5 minutes for security reasons.

            If you encounter any issues, feel free to contact our customer support team.

            Best regards,
            Support Team at %s

            ---

            📩 This is an automated email. Please do not reply to this message.
            """.formatted( resetLink, "ThinhGJaa");

        NotificationEvent event = NotificationEvent.builder()
                .channel("Email")
                .recipient(email)
                .subject("Reset Password")
                .body(content)
                .build();

        kafkaTemplate.send("password-reset-topic", parser.parseToJson(event));
    }
}
