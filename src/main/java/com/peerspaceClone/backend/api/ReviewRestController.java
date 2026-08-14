package com.peerspaceClone.backend.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peerspaceClone.backend.dto.ReviewInsertDTO;
import com.peerspaceClone.backend.dto.ReviewReadOnlyDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;
import com.peerspaceClone.backend.core.exception.ValidationException;
import com.peerspaceClone.backend.service.IReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewRestController {

    private final IReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewReadOnlyDTO> createReview(
            @Valid @RequestBody ReviewInsertDTO reviewInsertDto,
            BindingResult bindingResult) throws ValidationException, EntityAlreadyExistsException, EntityInvalidArgumentException, EntityNotFoundException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException("Review", "Invalid review details provided", bindingResult);
        }

        ReviewReadOnlyDTO review = reviewService.saveReview(reviewInsertDto);
        URI location = URI.create("/api/v1/reviews/" + review.id());
        return ResponseEntity.created(location).body(review);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<ReviewReadOnlyDTO>> getReviewsByPropertyId(@PathVariable Long propertyId) throws EntityNotFoundException {
        List<ReviewReadOnlyDTO> reviews = reviewService.getReviewsByPropertyId(propertyId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewReadOnlyDTO>> getReviewsByRevieweeId(@PathVariable Long userId) throws EntityNotFoundException {
        List<ReviewReadOnlyDTO> reviews = reviewService.getReviewsByRevieweeId(userId);
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReviewReadOnlyDTO> deleteReview(@PathVariable Long id) throws EntityNotFoundException {
        ReviewReadOnlyDTO deletedReview = reviewService.deleteReviewById(id);
        return ResponseEntity.ok(deletedReview);
    }
}
