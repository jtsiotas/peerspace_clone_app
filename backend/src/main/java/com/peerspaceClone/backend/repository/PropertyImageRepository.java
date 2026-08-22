package com.peerspaceClone.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.peerspaceClone.backend.model.PropertyImage;
import java.util.List;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long> {
    List<PropertyImage> findByPropertyId(Long propertyId);
}
