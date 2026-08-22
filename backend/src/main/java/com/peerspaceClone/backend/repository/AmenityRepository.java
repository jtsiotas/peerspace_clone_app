package com.peerspaceClone.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.peerspaceClone.backend.model.Amenity;
import java.util.Optional;

public interface AmenityRepository extends JpaRepository<Amenity, Long>, JpaSpecificationExecutor<Amenity> {
    Optional<Amenity> findByName(String name);
}
