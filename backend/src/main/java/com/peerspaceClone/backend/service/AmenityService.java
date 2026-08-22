package com.peerspaceClone.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peerspaceClone.backend.dto.AmenityInsertDTO;
import com.peerspaceClone.backend.dto.AmenityReadOnlyDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;
import com.peerspaceClone.backend.mapper.Mapper;
import com.peerspaceClone.backend.model.Amenity;
import com.peerspaceClone.backend.repository.AmenityRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AmenityService implements IAmenityService {

    private final AmenityRepository amenityRepository;
    private final Mapper mapper;

    @Override
    @Transactional(rollbackFor = EntityAlreadyExistsException.class)
    public AmenityReadOnlyDTO saveAmenity(AmenityInsertDTO dto) throws EntityAlreadyExistsException {
        try {
            log.info("Saving amenity: {}", dto.name());
            
            if (amenityRepository.findByName(dto.name()).isPresent()) {
                throw new EntityAlreadyExistsException("Amenity", "Amenity with name '" + dto.name() + "' already exists");
            }

            Amenity amenity = mapper.mapToAmenityEntity(dto);
            amenityRepository.save(amenity);
            log.info("Amenity saved successfully with ID: {}", amenity.getId());
            return mapper.mapToAmenityReadOnlyDTO(amenity);
        } catch (EntityAlreadyExistsException e) {
            log.error("Failed to save amenity: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error saving amenity", e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AmenityReadOnlyDTO> getAllAmenities() {
        try {
            log.info("Fetching all amenities");
            return amenityRepository.findAll().stream()
                    .map(mapper::mapToAmenityReadOnlyDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Unexpected error fetching amenities", e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AmenityReadOnlyDTO getAmenityById(Long id) throws EntityNotFoundException {
        try {
            log.info("Fetching amenity with ID: {}", id);
            Amenity amenity = amenityRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Amenity", "Amenity with ID=" + id + " not found"));
            return mapper.mapToAmenityReadOnlyDTO(amenity);
        } catch (EntityNotFoundException e) {
            log.error("Failed to fetch amenity: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error fetching amenity with ID: {}", id, e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteAmenityById(Long id) throws EntityNotFoundException {
        try {
            log.info("Deleting amenity with ID: {}", id);
            if (!amenityRepository.existsById(id)) {
                throw new EntityNotFoundException("Amenity", "Amenity with ID=" + id + " not found");
            }
            amenityRepository.deleteById(id);
            log.info("Amenity with ID: {} deleted successfully", id);
        } catch (EntityNotFoundException e) {
            log.error("Failed to delete amenity: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error deleting amenity with ID: {}", id, e);
            throw e;
        }
    }
}
