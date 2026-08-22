package com.peerspaceClone.backend.model;

import jakarta.persistence.*;
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

import java.time.Instant;
import com.peerspaceClone.backend.model.BookingStatus;

@Entity
@NoArgsConstructor
// @AllArgsConstructor
@Getter
@Setter
@Table(name = "bookings")
public class Booking extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    private User guest;
    @Column(nullable = false)
    private Instant startDatetime;

    @Column(nullable = false)
    private Instant endDatetime;
    @Column(nullable = false)
    private BigDecimal totalHours;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal propertyRate;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal hostFee;
    @Column(name="host_payout", precision = 10, scale = 2)
    private BigDecimal hostPayout;
    @Column(nullable = false, precision = 10, scale = 2 )
    private BigDecimal guestFee;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(name="cancelation_policy")
    private String cancellationPolicy;

    private String canceledBy;

    private Instant cancellationDate;

    private String cancelationReason;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY)
    private Set<Payment> payments = new HashSet<>();

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY)
    private Set<Message> messages = new HashSet<>();

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

    public Set<Payment> getAllPayments() {
        return Set.copyOf(payments);
    }

    public void addPayment(Payment payment) {
        if (payments == null)
            payments = new HashSet<>();
        payments.add(payment);
        payment.setBooking(this);
    }

    public void removePayment(Payment payment) {
        payments.remove(payment);
        payment.setBooking(null);
    }

    public Set<Message> getAllMessages() {
        return Set.copyOf(messages);
    }

    public void addMessage(Message message) {
        if (messages == null)
            messages = new HashSet<>();
        messages.add(message);
        message.setBooking(this);
    }

    public void removeMessage(Message message) {
        messages.remove(message);
        message.setBooking(null);
    }

    public Set<Review> getAllReviews() {
        return Set.copyOf(reviews);
    }

    public void addReview(Review review) {
        if (reviews == null)
            reviews = new HashSet<>();
        reviews.add(review);
        review.setBooking(this);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
        review.setBooking(null);
    }

    

}
