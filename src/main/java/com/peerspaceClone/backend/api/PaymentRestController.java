package com.peerspaceClone.backend.api;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peerspaceClone.backend.dto.PaymentInsertDTO;
import com.peerspaceClone.backend.dto.PaymentRefundDTO;
import com.peerspaceClone.backend.dto.PaymentReadOnlyDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;
import com.peerspaceClone.backend.core.exception.ValidationException;
import com.peerspaceClone.backend.service.IPaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentRestController {

    private final IPaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentReadOnlyDTO> createPayment(
            @Valid @RequestBody PaymentInsertDTO paymentInsertDto,
            BindingResult bindingResult) throws ValidationException, EntityAlreadyExistsException, EntityNotFoundException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException("Payment", "Invalid payment inputs", bindingResult);
        }

        PaymentReadOnlyDTO payment = paymentService.savePayment(paymentInsertDto);
        URI location = URI.create("/api/v1/payments/" + payment.id());
        return ResponseEntity.created(location).body(payment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentReadOnlyDTO> getPaymentById(@PathVariable Long id) throws EntityNotFoundException {
        PaymentReadOnlyDTO payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentReadOnlyDTO> getPaymentByBookingId(@PathVariable Long bookingId) throws EntityNotFoundException {
        PaymentReadOnlyDTO payment = paymentService.getPaymentByBookingId(bookingId);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentReadOnlyDTO> refundPayment(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRefundDTO paymentRefundDto,
            BindingResult bindingResult) throws ValidationException, EntityNotFoundException, EntityInvalidArgumentException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException("Payment", "Invalid refund inputs", bindingResult);
        }

        PaymentReadOnlyDTO payment = paymentService.refundPayment(id, paymentRefundDto);
        return ResponseEntity.ok(payment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PaymentReadOnlyDTO> deletePayment(@PathVariable Long id) throws EntityNotFoundException {
        PaymentReadOnlyDTO deletedPayment = paymentService.deletePaymentById(id);
        return ResponseEntity.ok(deletedPayment);
    }
}
