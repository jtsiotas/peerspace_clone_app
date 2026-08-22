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

@Entity
@NoArgsConstructor
// @AllArgsConstructor
@Getter
@Setter
@Table(name = "properties")
public class Property extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private User host;

    @Column(unique = true)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false, unique = true)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyStatus status = PropertyStatus.PENDING;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal halfDayRate;

    @Column(nullable = false)
    private String zip;

    @Column(nullable = false)
    private String timezone;

    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    private int sizeSqm;

    private int capacity;

    private int minHours;

    private int maxHours;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PropertyType type;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY)
    private Set<PropertyImage> propertyImages = new HashSet<>();

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY)
    private Set<BlockedSlot> blockedSlots = new HashSet<>();

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY)
    private Set<Booking> bookings = new HashSet<>();

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "properties_amenities", joinColumns = @JoinColumn(name = "property_id"), inverseJoinColumns = @JoinColumn(name = "amenity_id"))
    private Set<Amenity> amenities = new HashSet<>();

    public Set<PropertyImage> getAllPropertyImages() {
        return Set.copyOf(propertyImages);
    }

    public void addPropertyImage(PropertyImage propertyImage) {
        if (propertyImages == null)
            propertyImages = new HashSet<>();
        propertyImages.add(propertyImage);
        propertyImage.setProperty(this);
    }

    public void removePropertyImage(PropertyImage propertyImage) {
        propertyImages.remove(propertyImage);
        propertyImage.setProperty(null);
    }

    public Set<BlockedSlot> getAllBlockedSlots() {
        return Set.copyOf(blockedSlots);
    }

    public void addBlockedSlot(BlockedSlot blockedSlot) {
        if (blockedSlots == null)
            blockedSlots = new HashSet<>();
        blockedSlots.add(blockedSlot);
        blockedSlot.setProperty(this);
    }

    public void removeBlockedSlot(BlockedSlot blockedSlot) {
        blockedSlots.remove(blockedSlot);
        blockedSlot.setProperty(null);
    }

    public Set<Booking> getAllBookings() {
        return Set.copyOf(bookings);
    }

    public void addBooking(Booking booking) {
        if (bookings == null)
            bookings = new HashSet<>();
        bookings.add(booking);
        booking.setProperty(this);
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
        booking.setProperty(null);
    }

    public Set<Amenity> getAllAmenities() {
        return Set.copyOf(amenities);
    }

    public void addAmenity(Amenity amenity) {
        if (amenities == null)
            amenities = new HashSet<>();
        amenities.add(amenity);
        amenity.getProperties().add(this);
    }

    public void removeAmenity(Amenity amenity) {
        amenities.remove(amenity);
        amenity.getProperties().remove(this);
    }

}
