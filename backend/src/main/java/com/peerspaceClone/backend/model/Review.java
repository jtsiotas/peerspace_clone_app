package com.peerspaceClone.backend.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.persistence.*;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
// @AllArgsConstructor
@Getter
@Setter
@Table(name = "reviews", uniqueConstraints = @UniqueConstraint(columnNames = {"booking_id", "reviewer_id"}))
public class Review extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_id", nullable = false)
    private User reviewee;
    
    @Column(name = "reviewer_role", nullable = false)
    private String reviewerRole;
    
    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private int rating;
    
    @Column(columnDefinition = "TEXT")
    private String comment;

    
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = true;
    
}
