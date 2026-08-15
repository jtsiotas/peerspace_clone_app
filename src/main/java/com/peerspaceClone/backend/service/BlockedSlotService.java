package com.peerspaceClone.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peerspaceClone.backend.dto.BlockedSlotInsertDTO;
import com.peerspaceClone.backend.dto.BlockedSlotReadOnlyDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;
import com.peerspaceClone.backend.mapper.Mapper;
import com.peerspaceClone.backend.model.BlockedSlot;
import com.peerspaceClone.backend.model.Property;
import com.peerspaceClone.backend.repository.BlockedSlotRepository;
import com.peerspaceClone.backend.repository.PropertyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlockedSlotService implements IBlockedSlotService {

    private final BlockedSlotRepository blockedSlotRepository;
    private final PropertyRepository propertyRepository;
    private final Mapper mapper;

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class, EntityInvalidArgumentException.class})
    public BlockedSlotReadOnlyDTO saveBlockedSlot(BlockedSlotInsertDTO dto) throws EntityAlreadyExistsException, EntityInvalidArgumentException, EntityNotFoundException {
        try {
            log.info("Blocking slot for property ID: {} from {} to {}", dto.propertyId(), dto.startTime(), dto.endTime());

            Property property = propertyRepository.findById(dto.propertyId())
                    .filter(p -> !p.isDeleted())
                    .orElseThrow(() -> new EntityNotFoundException("Property", "Property with ID=" + dto.propertyId() + " not found"));

            if (dto.startTime().isAfter(dto.endTime()) || dto.startTime().isEqual(dto.endTime())) {
                throw new EntityInvalidArgumentException("BlockedSlot", "Start time must be strictly before end time");
            }

            // Check for overlapping active blocked slots
            boolean overlaps = blockedSlotRepository.existsOverlappingBlockedSlot(dto.propertyId(), dto.startTime(), dto.endTime());
            if (overlaps) {
                throw new EntityAlreadyExistsException("BlockedSlot", "Requested time slot overlaps with an existing blocked slot");
            }

            BlockedSlot blockedSlot = mapper.mapToBlockedSlotEntity(dto);
            blockedSlot.setProperty(property);

            blockedSlotRepository.save(blockedSlot);
            log.info("Slot blocked successfully with ID: {}", blockedSlot.getId());

            return mapper.mapToBlockedSlotReadOnlyDTO(blockedSlot);

        } catch (EntityNotFoundException | EntityAlreadyExistsException | EntityInvalidArgumentException e) {
            log.error("Failed to block slot: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error blocking slot", e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlockedSlotReadOnlyDTO> getBlockedSlotsByPropertyId(Long propertyId) throws EntityNotFoundException {
        try {
            log.info("Fetching blocked slots for property ID: {}", propertyId);
            if (!propertyRepository.existsById(propertyId)) {
                throw new EntityNotFoundException("Property", "Property with ID=" + propertyId + " not found");
            }

            List<BlockedSlot> slots = blockedSlotRepository.findByPropertyIdAndDeletedFalse(propertyId);
            return slots.stream()
                    .map(mapper::mapToBlockedSlotReadOnlyDTO)
                    .collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            log.error("Failed to fetch blocked slots: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error fetching blocked slots for property ID: {}", propertyId, e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public BlockedSlotReadOnlyDTO deleteBlockedSlotById(Long id) throws EntityNotFoundException {
        try {
            log.info("Soft deleting blocked slot with ID: {}", id);
            BlockedSlot blockedSlot = blockedSlotRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new EntityNotFoundException("BlockedSlot", "Active BlockedSlot with ID=" + id + " not found"));

            blockedSlot.softDelete();
            blockedSlotRepository.save(blockedSlot);
            log.info("Blocked slot with ID: {} soft deleted successfully", id);

            return mapper.mapToBlockedSlotReadOnlyDTO(blockedSlot);
        } catch (EntityNotFoundException e) {
            log.error("Failed to delete blocked slot: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error soft deleting blocked slot with ID: {}", id, e);
            throw e;
        }
    }
}
