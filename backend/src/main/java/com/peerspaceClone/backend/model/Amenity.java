package com.peerspaceClone.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
//@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "amenities")
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String iconUrl;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.NONE)
    // edw einai to asthenes meros tou many to many relationship.
    @ManyToMany(mappedBy = "amenities", fetch = FetchType.LAZY)
    private Set<Property> properties = new HashSet<>();

    public Set<Property> getAllProperties() {
        return Set.copyOf(properties);
    }

    public void addProperty(Property property) {
        properties.add(property);
        property.getAmenities().add(this);
    }

    public void removeProperty(Property property) {
        properties.remove(property);
        property.getAmenities().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Amenity that))
            return false;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }
}
