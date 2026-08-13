package com.peerspaceClone.backend.api;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peerspaceClone.backend.dto.BookingInsertDTO;
import com.peerspaceClone.backend.dto.BookingReadOnlyDTO;
import com.peerspaceClone.backend.dto.BookingCancelDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;
import com.peerspaceClone.backend.core.exception.ValidationException;
import com.peerspaceClone.backend.service.IBookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingRestController {

    private final IBookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingReadOnlyDTO> createBooking(
            @Valid @RequestBody BookingInsertDTO bookingInsertDto,
            BindingResult bindingResult) throws ValidationException, EntityAlreadyExistsException, EntityInvalidArgumentException, EntityNotFoundException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException("Booking", "Invalid booking inputs", bindingResult);
        }

        BookingReadOnlyDTO bookingReadOnlyDTO = bookingService.saveBooking(bookingInsertDto);
        URI location = URI.create("/api/v1/bookings/" + bookingReadOnlyDTO.id());
        return ResponseEntity.created(location).body(bookingReadOnlyDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingReadOnlyDTO> getBookingById(@PathVariable Long id) throws EntityNotFoundException {
        BookingReadOnlyDTO booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<Page<BookingReadOnlyDTO>> getBookingsByGuestId(
            @PathVariable Long guestId, 
            Pageable pageable) throws EntityNotFoundException {
        Page<BookingReadOnlyDTO> bookings = bookingService.getBookingsByGuestId(guestId, pageable);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<Page<BookingReadOnlyDTO>> getBookingsByPropertyId(
            @PathVariable Long propertyId, 
            Pageable pageable) throws EntityNotFoundException {
        Page<BookingReadOnlyDTO> bookings = bookingService.getBookingsByPropertyId(propertyId, pageable);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingReadOnlyDTO> cancelBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingCancelDTO bookingCancelDto,
            BindingResult bindingResult) throws ValidationException, EntityNotFoundException, EntityInvalidArgumentException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException("Booking", "Invalid cancellation inputs", bindingResult);
        }

        BookingReadOnlyDTO cancelledBooking = bookingService.cancelBooking(id, bookingCancelDto);
        return ResponseEntity.ok(cancelledBooking);
    }
}
