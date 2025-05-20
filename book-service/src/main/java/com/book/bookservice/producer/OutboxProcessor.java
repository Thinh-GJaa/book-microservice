package com.book.bookservice.producer;

import com.book.identityservice.entity.OutboxEvent;
import com.book.identityservice.enums.OutboxEventStatus;
import com.book.identityservice.repository.OutboxEventRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OutboxProcessor {

    KafkaTemplate<String, String> kafkaTemplate;

    OutboxEventRepository outboxEventRepository;

    @Scheduled(fixedDelay = 3000) // Mỗi 3 giây
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2)) // Retry tối// đa 3 lần
    public void processOutboxEvents() {
        List<OutboxEvent> events = outboxEventRepository.findByStatus(OutboxEventStatus.PENDING);

        for (OutboxEvent event : events) {
            try {
                // Gửi message lên Kafka
                kafkaTemplate.send(buildTopicName(event), event.getAggregateId(), event.getPayload());

                log.info("[OUTBOX PROCESSOR]: Gửi thành công topic: {}, Playload: {} "
                        , buildTopicName(event), event.getPayload());

                // Đánh dấu là đã gửi
                event.setStatus(OutboxEventStatus.SENT);
            } catch (Exception e) {
                // Lỗi gửi, đánh dấu FAILED hoặc giữ nguyên để retry
                event.setStatus(OutboxEventStatus.FAILED);
            }
        }

        outboxEventRepository.saveAll(events);
    }


    @Recover
    private void recoverException(Exception e){
        log.error("[OUTBOX PROCESSOR] [ERROR]: Xảy ra lỗi khi gửi message.{}", e.getMessage());
    }

    //Format topic
    private String buildTopicName(OutboxEvent event) {
        return event.getAggregateType() + "-" + event.getEventType() + "-topic";
    }
}
