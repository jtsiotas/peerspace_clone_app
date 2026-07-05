package com.peerspaceClone.backend.repository;
import org.springframework.data.jpa.repository.EntityGraph;

import org.springframework.data.jpa.repository.JpaRepository;
import com.peerspaceClone.backend.model.Review;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    //Review is @ManyToOne and any review list needs the reviewer so fetch it eagerly to avoid extra queries
    @EntityGraph(attributePaths={"reviewer"})
    List<Review> findByBookingId(Long bookingId);

    @EntityGraph(attributePaths={"reviewer"})
    List<Review> findByRevieweeId(Long revieweeId);
}
