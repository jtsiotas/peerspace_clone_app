package com.peerspaceClone.backend.mapper;

import org.springframework.stereotype.Component;
import java.util.stream.Collectors;
import com.peerspaceClone.backend.model.Role;
import com.peerspaceClone.backend.dto.UserInsertDTO;
import com.peerspaceClone.backend.dto.UserReadOnlyDTO;
import com.peerspaceClone.backend.dto.UserUpdateDTO;
import com.peerspaceClone.backend.dto.PropertyInsertDTO;
import com.peerspaceClone.backend.dto.PropertyReadOnlyDTO;
import com.peerspaceClone.backend.dto.PropertyUpdateDTO;
import com.peerspaceClone.backend.dto.BookingInsertDTO;
import com.peerspaceClone.backend.dto.BookingReadOnlyDTO;
import com.peerspaceClone.backend.dto.ReviewInsertDTO;
import com.peerspaceClone.backend.dto.ReviewReadOnlyDTO;
import com.peerspaceClone.backend.dto.MessageInsertDTO;
import com.peerspaceClone.backend.dto.MessageReadOnlyDTO;
import com.peerspaceClone.backend.dto.AmenityInsertDTO;
import com.peerspaceClone.backend.dto.AmenityReadOnlyDTO;
import com.peerspaceClone.backend.dto.PaymentInsertDTO;
import com.peerspaceClone.backend.dto.PaymentReadOnlyDTO;
import com.peerspaceClone.backend.dto.BlockedSlotInsertDTO;
import com.peerspaceClone.backend.dto.BlockedSlotReadOnlyDTO;
import com.peerspaceClone.backend.model.User;
import com.peerspaceClone.backend.model.Property;
import com.peerspaceClone.backend.model.Booking;
import com.peerspaceClone.backend.model.Review;
import com.peerspaceClone.backend.model.Message;
import com.peerspaceClone.backend.model.Amenity;
import com.peerspaceClone.backend.model.Payment;
import com.peerspaceClone.backend.model.BlockedSlot;

@Component
public class Mapper {

    public User mapToUserEntity(UserInsertDTO userInsertDto) {
        User user = new User();
        user.setUsername(userInsertDto.username());
        user.setEmail(userInsertDto.email());
        user.setFirstname(userInsertDto.firstName());
        user.setLastname(userInsertDto.lastName());
        return user;
    }
    
    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(
            user.getId(), 
            user.getUuid(),
            user.getUsername(), 
            user.getEmail(), 
            user.getFirstname(), 
            user.getLastname(), 
            user.getAllRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet())
        );
    }

    public void updateUserEntity(User user, UserUpdateDTO dto) {
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setFirstname(dto.firstName());
        user.setLastname(dto.lastName());
    }

    public Property mapToPropertyEntity(PropertyInsertDTO dto) {
        Property property = new Property();
        property.setTitle(dto.title());
        property.setDescription(dto.description());
        property.setCity(dto.city());
        property.setAddress(dto.address());
        property.setStatus(dto.status());
        property.setHourlyRate(dto.hourlyRate());
        property.setHalfDayRate(dto.halfDayRate());
        property.setZip(dto.zip());
        property.setTimezone(dto.timezone());
        property.setLongitude(dto.longitude());
        property.setLatitude(dto.latitude());
        property.setSizeSqm(dto.sizeSqm());
        property.setCapacity(dto.capacity());
        property.setMinHours(dto.minHours());
        property.setMaxHours(dto.maxHours());
        property.setType(dto.type());
        return property;
    }

    public PropertyReadOnlyDTO mapToPropertyReadOnlyDTO(Property property) {
        return new PropertyReadOnlyDTO(
            property.getId(),
            property.getHost() != null ? property.getHost().getId() : null,
            property.getTitle(),
            property.getDescription(),
            property.getCity(),
            property.getAddress(),
            property.getStatus(),
            property.getHourlyRate(),
            property.getHalfDayRate(),
            property.getZip(),
            property.getTimezone(),
            property.getLongitude(),
            property.getLatitude(),
            property.getSizeSqm(),
            property.getCapacity(),
            property.getMinHours(),
            property.getMaxHours(),
            property.getType()
        );
    }

    public void updatePropertyEntity(Property property, PropertyUpdateDTO dto) {
        property.setTitle(dto.title());
        property.setDescription(dto.description());
        property.setStatus(dto.status());
        property.setHourlyRate(dto.hourlyRate());
        property.setHalfDayRate(dto.halfDayRate());
        property.setCapacity(dto.capacity());
        property.setMinHours(dto.minHours());
        property.setMaxHours(dto.maxHours());
        property.setType(dto.type());
    }

    public Booking mapToBookingEntity(BookingInsertDTO dto) {
        Booking booking = new Booking();
        booking.setStartDatetime(dto.startDatetime());
        booking.setEndDatetime(dto.endDatetime());
        return booking;
    }

    public BookingReadOnlyDTO mapToBookingReadOnlyDTO(Booking booking) {
        return new BookingReadOnlyDTO(
            booking.getId(),
            booking.getProperty() != null ? booking.getProperty().getId() : null,
            booking.getProperty() != null ? booking.getProperty().getTitle() : null,
            booking.getGuest() != null ? booking.getGuest().getId() : null,
            booking.getGuest() != null ? booking.getGuest().getUsername() : null,
            booking.getStartDatetime(),
            booking.getEndDatetime(),
            booking.getTotalHours(),
            booking.getPropertyRate(),
            booking.getSubtotal(),
            booking.getHostFee(),
            booking.getGuestFee(),
            booking.getTotalAmount(),
            booking.getHostPayout(),
            booking.getStatus(),
            booking.getCancellationPolicy(),
            booking.getCanceledBy(),
            booking.getCancellationDate(),
            booking.getCancelationReason()
        );
     }

    public Review mapToReviewEntity(ReviewInsertDTO dto) {
        Review review = new Review();
        review.setRating(dto.rating());
        review.setComment(dto.comment());
        if (dto.isPublic() != null) {
            review.setIsPublic(dto.isPublic());
        }
        return review;
    }

    public ReviewReadOnlyDTO mapToReviewReadOnlyDTO(Review review) {
        return new ReviewReadOnlyDTO(
            review.getId(),
            review.getBooking() != null ? review.getBooking().getId() : null,
            review.getReviewer() != null ? review.getReviewer().getId() : null,
            review.getReviewer() != null ? review.getReviewer().getUsername() : null,
            review.getReviewee() != null ? review.getReviewee().getId() : null,
            review.getReviewerRole(),
            review.getRating(),
            review.getComment(),
            review.getIsPublic()
        );
    }

    public Message mapToMessageEntity(MessageInsertDTO dto) {
        Message message = new Message();
        message.setContent(dto.content());
        return message;
    }

    public MessageReadOnlyDTO mapToMessageReadOnlyDTO(Message message) {
        return new MessageReadOnlyDTO(
            message.getId(),
            message.getBooking() != null ? message.getBooking().getId() : null,
            message.getSender() != null ? message.getSender().getId() : null,
            message.getSender() != null ? message.getSender().getUsername() : null,
            message.getContent(),
            message.getCreatedAt()
        );
    }

    public Amenity mapToAmenityEntity(AmenityInsertDTO dto) {
        Amenity amenity = new Amenity();
        amenity.setName(dto.name());
        amenity.setIconUrl(dto.iconUrl());
        return amenity;
    }

    public AmenityReadOnlyDTO mapToAmenityReadOnlyDTO(Amenity amenity) {
        return new AmenityReadOnlyDTO(
            amenity.getId(),
            amenity.getName(),
            amenity.getIconUrl()
        );
    }

    public Payment mapToPaymentEntity(PaymentInsertDTO dto) {
        Payment payment = new Payment();
        payment.setAmount(dto.amount());
        payment.setCurrency(dto.currency());
        payment.setMethod(dto.method());
        return payment;
    }

    public PaymentReadOnlyDTO mapToPaymentReadOnlyDTO(Payment payment) {
        return new PaymentReadOnlyDTO(
            payment.getId(),
            payment.getBooking() != null ? payment.getBooking().getId() : null,
            payment.getAmount(),
            payment.getCurrency(),
            payment.getStatus() != null ? payment.getStatus().name() : null,
            payment.getMethod(),
            payment.getPaidAt(),
            payment.getRefundedAt(),
            payment.getRefundAmount()
        );
    }

    public BlockedSlot mapToBlockedSlotEntity(BlockedSlotInsertDTO dto) {
        BlockedSlot blockedSlot = new BlockedSlot();
        blockedSlot.setStartTime(dto.startTime());
        blockedSlot.setEndTime(dto.endTime());
        blockedSlot.setReason(dto.reason());
        return blockedSlot;
    }

    public BlockedSlotReadOnlyDTO mapToBlockedSlotReadOnlyDTO(BlockedSlot blockedSlot) {
        return new BlockedSlotReadOnlyDTO(
            blockedSlot.getId(),
            blockedSlot.getProperty() != null ? blockedSlot.getProperty().getId() : null,
            blockedSlot.getStartTime(),
            blockedSlot.getEndTime(),
            blockedSlot.getReason()
        );
    }
}