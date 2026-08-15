package com.peerspaceClone.backend.service;

import java.util.List;
import com.peerspaceClone.backend.dto.BlockedSlotInsertDTO;
import com.peerspaceClone.backend.dto.BlockedSlotReadOnlyDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;

public interface IBlockedSlotService {
    BlockedSlotReadOnlyDTO saveBlockedSlot(BlockedSlotInsertDTO dto) throws EntityAlreadyExistsException, EntityInvalidArgumentException, EntityNotFoundException;
    List<BlockedSlotReadOnlyDTO> getBlockedSlotsByPropertyId(Long propertyId) throws EntityNotFoundException;
    BlockedSlotReadOnlyDTO deleteBlockedSlotById(Long id) throws EntityNotFoundException;
}
