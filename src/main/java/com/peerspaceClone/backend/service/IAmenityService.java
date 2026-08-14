package com.peerspaceClone.backend.service;

import java.util.List;
import com.peerspaceClone.backend.dto.AmenityInsertDTO;
import com.peerspaceClone.backend.dto.AmenityReadOnlyDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;

public interface IAmenityService {
    AmenityReadOnlyDTO saveAmenity(AmenityInsertDTO dto) throws EntityAlreadyExistsException;
    List<AmenityReadOnlyDTO> getAllAmenities();
    AmenityReadOnlyDTO getAmenityById(Long id) throws EntityNotFoundException;
    void deleteAmenityById(Long id) throws EntityNotFoundException;
}
