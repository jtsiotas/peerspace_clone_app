package com.peerspaceClone.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.peerspaceClone.backend.model.Booking;
import java.time.Instant;

public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
    @EntityGraph(attributePaths = { "guest", "property" })
    Page<Booking> findByGuestId(Long guestId, Pageable pageable);

    @EntityGraph(attributePaths = { "guest", "property" })
    Page<Booking> findByPropertyId(Long propertyId, Pageable pageable);

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.property.id = :propertyId AND b.status <> 'CANCELLED' AND b.startDatetime < :end AND b.endDatetime > :start")
    boolean existsOverlappingBooking(@Param("propertyId") Long propertyId, @Param("start") Instant start, @Param("end") Instant end);
}