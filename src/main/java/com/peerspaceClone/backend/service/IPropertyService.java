package com.peerspaceClone.backend.service;

import java.util.List;

import com.peerspaceClone.backend.dto.PropertyInsertDTO;
import com.peerspaceClone.backend.dto.PropertyReadOnlyDTO;
import com.peerspaceClone.backend.dto.PropertyUpdateDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;

public interface IPropertyService {
    PropertyReadOnlyDTO saveProperty(PropertyInsertDTO propertyInsertDto) throws EntityAlreadyExistsException, EntityInvalidArgumentException, EntityNotFoundException;

    PropertyReadOnlyDTO getPropertyById(Long id) throws EntityNotFoundException;
    PropertyReadOnlyDTO getPropertyByIdAndDeletedFalse(Long id) throws EntityNotFoundException;

    List<PropertyReadOnlyDTO> getPropertiesByHostId(Long hostId) throws EntityNotFoundException;
    List<PropertyReadOnlyDTO> getPropertiesByHostIdAndDeletedFalse(Long hostId) throws EntityNotFoundException;

    PropertyReadOnlyDTO updateProperty(Long id, PropertyUpdateDTO propertyUpdateDTO) throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException;
    PropertyReadOnlyDTO deletePropertyById(Long id) throws EntityNotFoundException;
}
