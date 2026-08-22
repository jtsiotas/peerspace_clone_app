package com.peerspaceClone.backend.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peerspaceClone.backend.dto.PropertyInsertDTO;
import com.peerspaceClone.backend.dto.PropertyReadOnlyDTO;
import com.peerspaceClone.backend.dto.PropertyUpdateDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;
import com.peerspaceClone.backend.core.exception.ValidationException;
import com.peerspaceClone.backend.service.IPropertyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
public class PropertyRestController {

    private final IPropertyService propertyService;

    @PostMapping
    public ResponseEntity<PropertyReadOnlyDTO> createProperty(
            @Valid @RequestBody PropertyInsertDTO propertyInsertDto, 
            BindingResult bindingResult) throws ValidationException, EntityAlreadyExistsException, EntityInvalidArgumentException, EntityNotFoundException {
        
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Property", "Invalid property data", bindingResult);
        }

        PropertyReadOnlyDTO propertyReadOnlyDTO = propertyService.saveProperty(propertyInsertDto);
        URI location = URI.create("/api/v1/properties/" + propertyReadOnlyDTO.id());
        return ResponseEntity.created(location).body(propertyReadOnlyDTO);
    }

    @GetMapping
    public ResponseEntity<java.util.List<PropertyReadOnlyDTO>> getAllProperties() {
        java.util.List<PropertyReadOnlyDTO> properties = propertyService.getAllPropertiesAndDeletedFalse();
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyReadOnlyDTO> getPropertyById(@PathVariable Long id) throws EntityNotFoundException {
        PropertyReadOnlyDTO property = propertyService.getPropertyByIdAndDeletedFalse(id);
        return ResponseEntity.ok(property);
    }

    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<PropertyReadOnlyDTO>> getPropertiesByHostId(@PathVariable Long hostId) throws EntityNotFoundException {
        List<PropertyReadOnlyDTO> properties = propertyService.getPropertiesByHostIdAndDeletedFalse(hostId);
        return ResponseEntity.ok(properties);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyReadOnlyDTO> updateProperty(
            @PathVariable Long id,
            @Valid @RequestBody PropertyUpdateDTO propertyUpdateDTO,
            BindingResult bindingResult) throws ValidationException, EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException {
        
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Property", "Invalid property update data", bindingResult);
        }

        PropertyReadOnlyDTO updatedProperty = propertyService.updateProperty(id, propertyUpdateDTO);
        return ResponseEntity.ok(updatedProperty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PropertyReadOnlyDTO> deleteProperty(@PathVariable Long id) throws EntityNotFoundException {
        PropertyReadOnlyDTO deletedProperty = propertyService.deletePropertyById(id);
        return ResponseEntity.ok(deletedProperty);
    }
}
