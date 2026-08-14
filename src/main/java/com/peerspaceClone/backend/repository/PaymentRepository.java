package com.peerspaceClone.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.peerspaceClone.backend.model.Payment;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByIdAndDeletedFalse(Long id);
    Optional<Payment> findByBookingIdAndDeletedFalse(Long bookingId);
}
