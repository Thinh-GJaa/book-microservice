package com.book.identityservice.repository;

import com.book.identityservice.entity.OutboxEvent;
import com.book.identityservice.enums.OutboxEventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, String> {

    List<OutboxEvent> findByStatus(OutboxEventStatus outboxEventStatus);

}
