package com.peerspaceClone.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peerspaceClone.backend.dto.PropertyInsertDTO;
import com.peerspaceClone.backend.dto.PropertyReadOnlyDTO;
import com.peerspaceClone.backend.dto.PropertyUpdateDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;
import com.peerspaceClone.backend.mapper.Mapper;
import com.peerspaceClone.backend.model.Property;
import com.peerspaceClone.backend.model.User;
import com.peerspaceClone.backend.repository.PropertyRepository;
import com.peerspaceClone.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PropertyService implements IPropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    @Override
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityInvalidArgumentException.class, EntityNotFoundException.class})
    public PropertyReadOnlyDTO saveProperty(PropertyInsertDTO propertyInsertDto) 
            throws EntityAlreadyExistsException, EntityInvalidArgumentException, EntityNotFoundException {
        try {
            User host = userRepository.findById(propertyInsertDto.hostId())
                    .orElseThrow(() -> new EntityNotFoundException("User", "Host with id=" + propertyInsertDto.hostId() + " not found"));

            if (propertyRepository.existsByTitle(propertyInsertDto.title())) {
                throw new EntityAlreadyExistsException("Property", "Property with title '" + propertyInsertDto.title() + "' already exists");
            }

            if (propertyRepository.existsByAddress(propertyInsertDto.address())) {
                throw new EntityAlreadyExistsException("Property", "Property at address '" + propertyInsertDto.address() + "' already exists");
            }

            Property property = mapper.mapToPropertyEntity(propertyInsertDto);
            property.setHost(host);

            propertyRepository.save(property);
            log.info("Property with title '" + propertyInsertDto.title() + "' saved successfully with ID: " + property.getId());
            return mapper.mapToPropertyReadOnlyDTO(property);

        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            log.error("Save property failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Save property failed due to an unexpected error");
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyReadOnlyDTO getPropertyById(Long id) throws EntityNotFoundException {
        try {
            Property property = propertyRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Property", "Property with id=" + id + " not found"));
            log.debug("Property with id=" + id + " found successfully");
            return mapper.mapToPropertyReadOnlyDTO(property);
        } catch (EntityNotFoundException e) {
            log.error("Get failed. Property with id=" + id + " not found");
            throw e;
        } catch (Exception e) {
            log.error("Get failed due to an unexpected error");
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyReadOnlyDTO getPropertyByIdAndDeletedFalse(Long id) throws EntityNotFoundException {
        try {
            Property property = propertyRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new EntityNotFoundException("Property", "Property with id=" + id + " not found"));
            log.debug("Property with id=" + id + " found successfully");
            return mapper.mapToPropertyReadOnlyDTO(property);
        } catch (EntityNotFoundException e) {
            log.error("Get failed. Property with id=" + id + " not found");
            throw e;
        } catch (Exception e) {
            log.error("Get failed due to an unexpected error");
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyReadOnlyDTO> getPropertiesByHostId(Long hostId) throws EntityNotFoundException {
        try {
            if (!userRepository.existsById(hostId)) {
                throw new EntityNotFoundException("User", "Host with id=" + hostId + " not found");
            }
            List<Property> properties = propertyRepository.findByHostId(hostId);
            log.debug("Found " + properties.size() + " properties for hostId=" + hostId);
            return properties.stream()
                    .map(mapper::mapToPropertyReadOnlyDTO)
                    .toList();
        } catch (EntityNotFoundException e) {
            log.error("Get by host failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Get by host failed due to an unexpected error");
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyReadOnlyDTO> getPropertiesByHostIdAndDeletedFalse(Long hostId) throws EntityNotFoundException {
        try {
            if (!userRepository.existsById(hostId)) {
                throw new EntityNotFoundException("User", "Host with id=" + hostId + " not found");
            }
            List<Property> properties = propertyRepository.findByHostIdAndDeletedFalse(hostId);
            log.debug("Found " + properties.size() + " active properties for hostId=" + hostId);
            return properties.stream()
                    .map(mapper::mapToPropertyReadOnlyDTO)
                    .toList();
        } catch (EntityNotFoundException e) {
            log.error("Get active properties by host failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Get active properties by host failed due to an unexpected error");
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class, EntityInvalidArgumentException.class})
    public PropertyReadOnlyDTO updateProperty(Long id, PropertyUpdateDTO propertyUpdateDTO)
            throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            Property existingProperty = propertyRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Property", "Property with id=" + id + " not found"));

            Optional<Property> propertyWithSameTitle = propertyRepository.findByTitle(propertyUpdateDTO.title());
            if (propertyWithSameTitle.isPresent() && !propertyWithSameTitle.get().getId().equals(existingProperty.getId())) {
                throw new EntityAlreadyExistsException("Property", "Property with title '" + propertyUpdateDTO.title() + "' already exists");
            }

            mapper.updatePropertyEntity(existingProperty, propertyUpdateDTO);
            propertyRepository.save(existingProperty);
            log.info("Property with id=" + id + " updated successfully");
            return mapper.mapToPropertyReadOnlyDTO(existingProperty);

        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            log.error("Update property failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Update property failed due to an unexpected error");
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public PropertyReadOnlyDTO deletePropertyById(Long id) throws EntityNotFoundException {
        try {
            Property property = propertyRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new EntityNotFoundException("Property", "Property with id=" + id + " not found or already deleted"));

            property.softDelete();
            propertyRepository.save(property);
            log.info("Property with id=" + id + " soft-deleted successfully");
            return mapper.mapToPropertyReadOnlyDTO(property);

        } catch (EntityNotFoundException e) {
            log.error("Delete property failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Delete property failed due to an unexpected error");
            throw e;
        }
    }
}