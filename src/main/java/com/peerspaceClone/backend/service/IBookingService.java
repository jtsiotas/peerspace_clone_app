package com.peerspaceClone.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.peerspaceClone.backend.dto.BookingInsertDTO;
import com.peerspaceClone.backend.dto.BookingReadOnlyDTO;
import com.peerspaceClone.backend.dto.BookingCancelDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;

public interface IBookingService {
    BookingReadOnlyDTO saveBooking(BookingInsertDTO bookingInsertDto) 
            throws EntityAlreadyExistsException, EntityInvalidArgumentException, EntityNotFoundException;

    BookingReadOnlyDTO getBookingById(Long id) throws EntityNotFoundException;

    Page<BookingReadOnlyDTO> getBookingsByGuestId(Long guestId, Pageable pageable) throws EntityNotFoundException;

    Page<BookingReadOnlyDTO> getBookingsByPropertyId(Long propertyId, Pageable pageable) throws EntityNotFoundException;

    BookingReadOnlyDTO cancelBooking(Long id, BookingCancelDTO bookingCancelDto) 
            throws EntityNotFoundException, EntityInvalidArgumentException;
}
