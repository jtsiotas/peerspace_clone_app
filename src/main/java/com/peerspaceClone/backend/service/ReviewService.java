package com.peerspaceClone.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peerspaceClone.backend.dto.ReviewInsertDTO;
import com.peerspaceClone.backend.dto.ReviewReadOnlyDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;
import com.peerspaceClone.backend.mapper.Mapper;
import com.peerspaceClone.backend.model.Booking;
import com.peerspaceClone.backend.model.BookingStatus;
import com.peerspaceClone.backend.model.Review;
import com.peerspaceClone.backend.model.User;
import com.peerspaceClone.backend.repository.BookingRepository;
import com.peerspaceClone.backend.repository.ReviewRepository;
import com.peerspaceClone.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class, EntityInvalidArgumentException.class})
    public ReviewReadOnlyDTO saveReview(ReviewInsertDTO dto) throws EntityAlreadyExistsException, EntityInvalidArgumentException, EntityNotFoundException {
        try {
            log.info("Saving review for booking ID: {}", dto.bookingId());

            Booking booking = bookingRepository.findById(dto.bookingId())
                    .orElseThrow(() -> new EntityNotFoundException("Booking", "Booking with ID=" + dto.bookingId() + " not found"));

            // Check if booking is completed
            if (booking.getStatus() != BookingStatus.COMPLETED) {
                throw new EntityInvalidArgumentException("Review", "Only completed bookings can be reviewed. Current status: " + booking.getStatus());
            }

            User reviewer = userRepository.findById(dto.reviewerId())
                    .orElseThrow(() -> new EntityNotFoundException("User", "Reviewer with ID=" + dto.reviewerId() + " not found"));

            // Validate relationship and determine role & reviewee
            String reviewerRole;
            User reviewee;

            if (booking.getGuest().getId().equals(dto.reviewerId())) {
                reviewerRole = "GUEST";
                reviewee = booking.getProperty().getHost();
            } else if (booking.getProperty().getHost().getId().equals(dto.reviewerId())) {
                reviewerRole = "HOST";
                reviewee = booking.getGuest();
            } else {
                throw new EntityInvalidArgumentException("Review", "User with ID=" + dto.reviewerId() + " is not a participant of this booking");
            }

            // Check for existing review by this reviewer for this booking
            Optional<Review> existingReview = reviewRepository.findByBookingIdAndReviewerIdAndDeletedFalse(dto.bookingId(), dto.reviewerId());
            if (existingReview.isPresent()) {
                throw new EntityAlreadyExistsException("Review", "A review has already been submitted for this booking by reviewer ID=" + dto.reviewerId());
            }

            Review review = mapper.mapToReviewEntity(dto);
            review.setBooking(booking);
            review.setReviewer(reviewer);
            review.setReviewee(reviewee);
            review.setReviewerRole(reviewerRole);

            reviewRepository.save(review);
            log.info("Review saved successfully with ID: {}", review.getId());
            return mapper.mapToReviewReadOnlyDTO(review);

        } catch (EntityNotFoundException | EntityAlreadyExistsException | EntityInvalidArgumentException e) {
            log.error("Review creation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Review creation failed with unexpected error", e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewReadOnlyDTO> getReviewsByPropertyId(Long propertyId) throws EntityNotFoundException {
        try {
            log.info("Fetching reviews for property ID: {}", propertyId);
            List<Review> reviews = reviewRepository.findByBookingPropertyIdAndDeletedFalse(propertyId);
            return reviews.stream()
                    .map(mapper::mapToReviewReadOnlyDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to fetch reviews for property ID: {}", propertyId, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewReadOnlyDTO> getReviewsByRevieweeId(Long revieweeId) throws EntityNotFoundException {
        try {
            log.info("Fetching reviews for reviewee ID: {}", revieweeId);
            List<Review> reviews = reviewRepository.findByRevieweeIdAndDeletedFalse(revieweeId);
            return reviews.stream()
                    .map(mapper::mapToReviewReadOnlyDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to fetch reviews for reviewee ID: {}", revieweeId, e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public ReviewReadOnlyDTO deleteReviewById(Long id) throws EntityNotFoundException {
        try {
            log.info("Soft deleting review with ID: {}", id);
            Review review = reviewRepository.findById(id)
                    .filter(r -> !r.isDeleted())
                    .orElseThrow(() -> new EntityNotFoundException("Review", "Active Review with ID=" + id + " not found"));

            review.softDelete();
            reviewRepository.save(review);
            log.info("Review with ID: {} soft deleted successfully", id);
            return mapper.mapToReviewReadOnlyDTO(review);
        } catch (EntityNotFoundException e) {
            log.error("Failed to delete review: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during review deletion", e);
            throw e;
        }
    }
}
