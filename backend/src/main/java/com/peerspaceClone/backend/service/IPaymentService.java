package com.peerspaceClone.backend.service;

import com.peerspaceClone.backend.dto.PaymentInsertDTO;
import com.peerspaceClone.backend.dto.PaymentRefundDTO;
import com.peerspaceClone.backend.dto.PaymentReadOnlyDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;

public interface IPaymentService {
    PaymentReadOnlyDTO savePayment(PaymentInsertDTO dto) throws EntityAlreadyExistsException, EntityNotFoundException;
    PaymentReadOnlyDTO getPaymentById(Long id) throws EntityNotFoundException;
    PaymentReadOnlyDTO getPaymentByBookingId(Long bookingId) throws EntityNotFoundException;
    PaymentReadOnlyDTO refundPayment(Long id, PaymentRefundDTO dto) throws EntityNotFoundException, EntityInvalidArgumentException;
    PaymentReadOnlyDTO deletePaymentById(Long id) throws EntityNotFoundException;
}
