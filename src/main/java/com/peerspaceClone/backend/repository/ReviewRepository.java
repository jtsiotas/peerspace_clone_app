package com.peerspaceClone.backend.repository;
import org.springframework.data.jpa.repository.EntityGraph;

import org.springframework.data.jpa.repository.JpaRepository;
import com.peerspaceClone.backend.model.Review;
import java.util.List;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    @EntityGraph(attributePaths={"reviewer", "booking"})
    List<Review> findByBookingIdAndDeletedFalse(Long bookingId);

    @EntityGraph(attributePaths={"reviewer", "booking"})
    List<Review> findByRevieweeIdAndDeletedFalse(Long revieweeId);

    @EntityGraph(attributePaths={"reviewer", "booking"})
    List<Review> findByBookingPropertyIdAndDeletedFalse(Long propertyId);

    Optional<Review> findByBookingIdAndReviewerIdAndDeletedFalse(Long bookingId, Long reviewerId);
}
