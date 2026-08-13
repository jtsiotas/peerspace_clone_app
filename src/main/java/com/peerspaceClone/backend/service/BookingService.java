package com.peerspaceClone.backend.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peerspaceClone.backend.dto.BookingInsertDTO;
import com.peerspaceClone.backend.dto.BookingReadOnlyDTO;
import com.peerspaceClone.backend.dto.BookingCancelDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;
import com.peerspaceClone.backend.mapper.Mapper;
import com.peerspaceClone.backend.model.Booking;
import com.peerspaceClone.backend.model.BookingStatus;
import com.peerspaceClone.backend.model.Property;
import com.peerspaceClone.backend.model.User;
import com.peerspaceClone.backend.repository.BookingRepository;
import com.peerspaceClone.backend.repository.BlockedSlotRepository;
import com.peerspaceClone.backend.repository.PropertyRepository;
import com.peerspaceClone.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService implements IBookingService {

    private final BookingRepository bookingRepository;
    private final BlockedSlotRepository blockedSlotRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    @Override
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityInvalidArgumentException.class, EntityNotFoundException.class})
    public BookingReadOnlyDTO saveBooking(BookingInsertDTO bookingInsertDto) 
            throws EntityAlreadyExistsException, EntityInvalidArgumentException, EntityNotFoundException {
        try {
            Property property = propertyRepository.findById(bookingInsertDto.propertyId())
                    .orElseThrow(() -> new EntityNotFoundException("Property", "Property with id=" + bookingInsertDto.propertyId() + " not found"));

            User guest = userRepository.findById(bookingInsertDto.guestId())
                    .orElseThrow(() -> new EntityNotFoundException("User", "Guest with id=" + bookingInsertDto.guestId() + " not found"));

            if (bookingInsertDto.startDatetime().isAfter(bookingInsertDto.endDatetime()) || 
                bookingInsertDto.startDatetime().equals(bookingInsertDto.endDatetime())) {
                throw new EntityInvalidArgumentException("Booking", "Start datetime must be before end datetime");
            }

            // Calculate hours duration
            java.time.Duration duration = java.time.Duration.between(bookingInsertDto.startDatetime(), bookingInsertDto.endDatetime());
            BigDecimal totalHours = BigDecimal.valueOf(duration.toMinutes())
                    .divide(BigDecimal.valueOf(60), 2, java.math.RoundingMode.HALF_UP);

            if (totalHours.doubleValue() < property.getMinHours()) {
                throw new EntityInvalidArgumentException("Booking", "Minimum booking duration is " + property.getMinHours() + " hours");
            }

            // Overlap check
            if (bookingRepository.existsOverlappingBooking(property.getId(), bookingInsertDto.startDatetime(), bookingInsertDto.endDatetime())) {
                throw new EntityAlreadyExistsException("Booking", "Property is already booked in the requested timeframe");
            }

            java.time.LocalDateTime startLdt = java.time.LocalDateTime.ofInstant(bookingInsertDto.startDatetime(), java.time.ZoneId.of("UTC"));
            java.time.LocalDateTime endLdt = java.time.LocalDateTime.ofInstant(bookingInsertDto.endDatetime(), java.time.ZoneId.of("UTC"));

            if (blockedSlotRepository.existsOverlappingBlockedSlot(property.getId(), startLdt, endLdt)) {
                throw new EntityAlreadyExistsException("Booking", "Property is blocked by the host in the requested timeframe");
            }

            BigDecimal hourlyRate = property.getHourlyRate();
            BigDecimal subtotal = hourlyRate.multiply(totalHours).setScale(2, java.math.RoundingMode.HALF_UP);
            
            // Peerspace fees configuration: 3% host platform fee, 10% guest fee
            BigDecimal hostFee = subtotal.multiply(BigDecimal.valueOf(0.03)).setScale(2, java.math.RoundingMode.HALF_UP);
            BigDecimal guestFee = subtotal.multiply(BigDecimal.valueOf(0.10)).setScale(2, java.math.RoundingMode.HALF_UP);
            BigDecimal totalAmount = subtotal.add(guestFee).setScale(2, java.math.RoundingMode.HALF_UP);
            BigDecimal hostPayout = subtotal.subtract(hostFee).setScale(2, java.math.RoundingMode.HALF_UP);

            Booking booking = mapper.mapToBookingEntity(bookingInsertDto);
            booking.setProperty(property);
            booking.setGuest(guest);
            booking.setTotalHours(totalHours);
            booking.setPropertyRate(hourlyRate);
            booking.setSubtotal(subtotal);
            booking.setHostFee(hostFee);
            booking.setGuestFee(guestFee);
            booking.setTotalAmount(totalAmount);
            booking.setHostPayout(hostPayout);
            booking.setStatus(BookingStatus.PENDING);

            bookingRepository.save(booking);
            log.info("Booking created successfully with ID: " + booking.getId());
            return mapper.mapToBookingReadOnlyDTO(booking);

        } catch (EntityNotFoundException | EntityAlreadyExistsException | EntityInvalidArgumentException e) {
            log.error("Create booking failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Create booking failed due to an unexpected error");
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BookingReadOnlyDTO getBookingById(Long id) throws EntityNotFoundException {
        try {
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Booking", "Booking with id=" + id + " not found"));
            log.debug("Booking with id=" + id + " found successfully");
            return mapper.mapToBookingReadOnlyDTO(booking);
        } catch (EntityNotFoundException e) {
            log.error("Get booking failed. Booking with id=" + id + " not found");
            throw e;
        } catch (Exception e) {
            log.error("Get booking failed due to an unexpected error");
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingReadOnlyDTO> getBookingsByGuestId(Long guestId, Pageable pageable) throws EntityNotFoundException {
        try {
            if (!userRepository.existsById(guestId)) {
                throw new EntityNotFoundException("User", "Guest with id=" + guestId + " not found");
            }
            Page<Booking> bookings = bookingRepository.findByGuestId(guestId, pageable);
            return bookings.map(mapper::mapToBookingReadOnlyDTO);
        } catch (EntityNotFoundException e) {
            log.error("Get guest bookings failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Get guest bookings failed due to an unexpected error");
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingReadOnlyDTO> getBookingsByPropertyId(Long propertyId, Pageable pageable) throws EntityNotFoundException {
        try {
            if (!propertyRepository.existsById(propertyId)) {
                throw new EntityNotFoundException("Property", "Property with id=" + propertyId + " not found");
            }
            Page<Booking> bookings = bookingRepository.findByPropertyId(propertyId, pageable);
            return bookings.map(mapper::mapToBookingReadOnlyDTO);
        } catch (EntityNotFoundException e) {
            log.error("Get property bookings failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Get property bookings failed due to an unexpected error");
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityInvalidArgumentException.class})
    public BookingReadOnlyDTO cancelBooking(Long id, BookingCancelDTO bookingCancelDto) 
            throws EntityNotFoundException, EntityInvalidArgumentException {
        try {
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Booking", "Booking with id=" + id + " not found"));

            if (booking.getStatus() == BookingStatus.CANCELLED) {
                throw new EntityInvalidArgumentException("Booking", "Booking is already cancelled");
            }
            if (booking.getStatus() == BookingStatus.COMPLETED) {
                throw new EntityInvalidArgumentException("Booking", "Completed bookings cannot be cancelled");
            }

            booking.setStatus(BookingStatus.CANCELLED);
            booking.setCanceledBy(bookingCancelDto.canceledBy());
            booking.setCancelationReason(bookingCancelDto.cancelationReason());
            booking.setCancellationDate(Instant.now());

            bookingRepository.save(booking);
            log.info("Booking with ID: " + id + " has been cancelled successfully");
            return mapper.mapToBookingReadOnlyDTO(booking);

        } catch (EntityNotFoundException | EntityInvalidArgumentException e) {
            log.error("Cancel booking failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Cancel booking failed due to an unexpected error");
            throw e;
        }
    }
}
