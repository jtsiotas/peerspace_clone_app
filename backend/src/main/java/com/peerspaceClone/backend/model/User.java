package com.peerspaceClone.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@NoArgsConstructor
// @AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User extends AbstractEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uid", unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @Column(unique = true)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
    name = "users_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    private String firstname;
    private String lastname;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "host", fetch = FetchType.LAZY)
    private Set<Property> properties = new HashSet<>();

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY)
    private Set<Booking> bookings = new HashSet<>();

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "reviewer", fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private Set<Message> messages = new HashSet<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) { this.uuid = uuid; }

    @Override
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (roles != null) {
            roles.forEach(role -> {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
                role.getCapabilities().forEach(capability ->
                    grantedAuthorities.add(new SimpleGrantedAuthority(capability.getName())));
            });
        }
        return grantedAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !isDeleted();
    }

    @PrePersist
    public void initializeUUID() {
        this.uuid = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user))
            return false;
        return Objects.equals(getUuid(), user.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUuid());
    }

    public Set<Property> getAllProperties() {
        return Set.copyOf(properties);
    }

    public void addProperty(Property property) {
        if (properties == null)
            properties = new HashSet<>();
        properties.add(property);
        property.setHost(this);
    }

    public void removeProperty(Property property) {
        if (properties == null)
            return;
        properties.remove(property);
        property.setHost(null);
    }

    public Set<Booking> getAllBookings() {
        return Set.copyOf(bookings);
    }

    public void addBooking(Booking booking) {
        if (bookings == null)
            bookings = new HashSet<>();
        bookings.add(booking);
        booking.setGuest(this);
    }

    public void removeBooking(Booking booking) {
        if (bookings == null)
            return;
        bookings.remove(booking);
        booking.setGuest(null);
    }

    public Set<Review> getAllReviews() {
        return Set.copyOf(reviews);
    }

    public void addReview(Review review) {
        if (reviews == null)
            reviews = new HashSet<>();
        reviews.add(review);
        review.setReviewer(this);
    }

    public void removeReview(Review review) {
        if (reviews == null)
            return;
        reviews.remove(review);
        review.setReviewer(null);
    }

    public Set<Message> getAllMessages() {
        return Set.copyOf(messages);
    }

    public void addMessage(Message message) {
        if (messages == null)
            messages = new HashSet<>();
        messages.add(message);
        message.setSender(this);
    }

    public void removeMessage(Message message) {
        if (messages == null)
            return;
        messages.remove(message);
        message.setSender(null);
    }

    public Set<Role> getAllRoles() {
    return Set.copyOf(roles);
    }

    public void addRole(Role role) {
        if (roles == null) roles = new HashSet<>();
        roles.add(role);
        role.getUsers().add(this); // see Role.java note below
    }

    public void removeRole(Role role) {
        if (roles == null) return;
        roles.remove(role);
        role.getUsers().remove(this);
    }
}
