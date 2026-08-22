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

import com.peerspaceClone.backend.dto.AmenityInsertDTO;
import com.peerspaceClone.backend.dto.AmenityReadOnlyDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;
import com.peerspaceClone.backend.core.exception.ValidationException;
import com.peerspaceClone.backend.service.IAmenityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/amenities")
@RequiredArgsConstructor
public class AmenityRestController {

    private final IAmenityService amenityService;

    @PostMapping
    public ResponseEntity<AmenityReadOnlyDTO> createAmenity(
            @Valid @RequestBody AmenityInsertDTO amenityInsertDto,
            BindingResult bindingResult) throws ValidationException, EntityAlreadyExistsException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException("Amenity", "Invalid amenity details provided", bindingResult);
        }

        AmenityReadOnlyDTO amenity = amenityService.saveAmenity(amenityInsertDto);
        URI location = URI.create("/api/v1/amenities/" + amenity.id());
        return ResponseEntity.created(location).body(amenity);
    }

    @GetMapping
    public ResponseEntity<List<AmenityReadOnlyDTO>> getAllAmenities() {
        List<AmenityReadOnlyDTO> amenities = amenityService.getAllAmenities();
        return ResponseEntity.ok(amenities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmenityReadOnlyDTO> getAmenityById(@PathVariable Long id) throws EntityNotFoundException {
        AmenityReadOnlyDTO amenity = amenityService.getAmenityById(id);
        return ResponseEntity.ok(amenity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Long id) throws EntityNotFoundException {
        amenityService.deleteAmenityById(id);
        return ResponseEntity.noContent().build();
    }
}
