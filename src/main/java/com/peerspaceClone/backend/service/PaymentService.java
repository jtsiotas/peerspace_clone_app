package com.peerspaceClone.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peerspaceClone.backend.dto.PaymentInsertDTO;
import com.peerspaceClone.backend.dto.PaymentRefundDTO;
import com.peerspaceClone.backend.dto.PaymentReadOnlyDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;
import com.peerspaceClone.backend.mapper.Mapper;
import com.peerspaceClone.backend.model.Booking;
import com.peerspaceClone.backend.model.Payment;
import com.peerspaceClone.backend.model.PaymentStatus;
import com.peerspaceClone.backend.repository.BookingRepository;
import com.peerspaceClone.backend.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final Mapper mapper;

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class, EntityInvalidArgumentException.class})
    public PaymentReadOnlyDTO savePayment(PaymentInsertDTO dto) throws EntityAlreadyExistsException, EntityNotFoundException {
        try {
            log.info("Processing payment for booking ID: {}", dto.bookingId());

            Booking booking = bookingRepository.findById(dto.bookingId())
                    .orElseThrow(() -> new EntityNotFoundException("Booking", "Booking with ID=" + dto.bookingId() + " not found"));

            // Check if booking already has active non-failed payments
            Optional<Payment> existingPayment = paymentRepository.findByBookingIdAndDeletedFalse(dto.bookingId());
            if (existingPayment.isPresent()) {
                PaymentStatus status = existingPayment.get().getStatus();
                if (status == PaymentStatus.COMPLETED || status == PaymentStatus.PENDING || status == PaymentStatus.REFUNDED) {
                    throw new EntityAlreadyExistsException("Payment", "Active payment already exists for booking ID=" + dto.bookingId() + " with status: " + status);
                }
            }

            Payment payment = mapper.mapToPaymentEntity(dto);
            payment.setBooking(booking);
            payment.setStatus(PaymentStatus.COMPLETED); // Mock gateway success
            payment.setPaidAt(LocalDateTime.now());
            payment.setRefundAmount(BigDecimal.ZERO);

            paymentRepository.save(payment);
            log.info("Payment processed successfully with ID: {}", payment.getId());
            return mapper.mapToPaymentReadOnlyDTO(payment);

        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            log.error("Payment processing failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during payment processing", e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentReadOnlyDTO getPaymentById(Long id) throws EntityNotFoundException {
        try {
            log.info("Fetching payment with ID: {}", id);
            Payment payment = paymentRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new EntityNotFoundException("Payment", "Active Payment with ID=" + id + " not found"));
            return mapper.mapToPaymentReadOnlyDTO(payment);
        } catch (EntityNotFoundException e) {
            log.error("Payment fetch failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error fetching payment with ID: {}", id, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentReadOnlyDTO getPaymentByBookingId(Long bookingId) throws EntityNotFoundException {
        try {
            log.info("Fetching payment for booking ID: {}", bookingId);
            Payment payment = paymentRepository.findByBookingIdAndDeletedFalse(bookingId)
                    .orElseThrow(() -> new EntityNotFoundException("Payment", "Active Payment for booking ID=" + bookingId + " not found"));
            return mapper.mapToPaymentReadOnlyDTO(payment);
        } catch (EntityNotFoundException e) {
            log.error("Payment fetch failed for booking ID {}: {}", bookingId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error fetching payment for booking ID: {}", bookingId, e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityInvalidArgumentException.class})
    public PaymentReadOnlyDTO refundPayment(Long id, PaymentRefundDTO dto) throws EntityNotFoundException, EntityInvalidArgumentException {
        try {
            log.info("Refunding payment with ID: {} for amount: {}", id, dto.refundAmount());

            Payment payment = paymentRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new EntityNotFoundException("Payment", "Active Payment with ID=" + id + " not found"));

            if (payment.getStatus() != PaymentStatus.COMPLETED) {
                throw new EntityInvalidArgumentException("Payment", "Only completed payments can be refunded. Current status: " + payment.getStatus());
            }

            if (dto.refundAmount().compareTo(payment.getAmount()) > 0) {
                throw new EntityInvalidArgumentException("Payment", "Refund amount cannot exceed original payment amount of " + payment.getAmount());
            }

            payment.setStatus(PaymentStatus.REFUNDED);
            payment.setRefundAmount(dto.refundAmount());
            payment.setRefundedAt(LocalDateTime.now());

            paymentRepository.save(payment);
            log.info("Payment with ID: {} refunded successfully", payment.getId());
            return mapper.mapToPaymentReadOnlyDTO(payment);

        } catch (EntityNotFoundException | EntityInvalidArgumentException e) {
            log.error("Refund failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during refund processing", e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public PaymentReadOnlyDTO deletePaymentById(Long id) throws EntityNotFoundException {
        try {
            log.info("Soft deleting payment with ID: {}", id);
            Payment payment = paymentRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new EntityNotFoundException("Payment", "Active Payment with ID=" + id + " not found"));

            payment.softDelete();
            paymentRepository.save(payment);
            log.info("Payment with ID: {} soft deleted successfully", id);
            return mapper.mapToPaymentReadOnlyDTO(payment);
        } catch (EntityNotFoundException e) {
            log.error("Failed to delete payment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error soft deleting payment with ID: {}", id, e);
            throw e;
        }
    }
}
