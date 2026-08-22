package com.peerspaceClone.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.peerspaceClone.backend.model.Message;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @EntityGraph(attributePaths = {"sender", "booking"})
    List<Message> findByBookingIdAndDeletedFalseOrderByCreatedAtAsc(Long bookingId);
}
