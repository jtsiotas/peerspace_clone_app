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

import com.peerspaceClone.backend.dto.BlockedSlotInsertDTO;
import com.peerspaceClone.backend.dto.BlockedSlotReadOnlyDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;
import com.peerspaceClone.backend.core.exception.ValidationException;
import com.peerspaceClone.backend.service.IBlockedSlotService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/blocked-slots")
@RequiredArgsConstructor
public class BlockedSlotRestController {

    private final IBlockedSlotService blockedSlotService;

    @PostMapping
    public ResponseEntity<BlockedSlotReadOnlyDTO> createBlockedSlot(
            @Valid @RequestBody BlockedSlotInsertDTO blockedSlotInsertDto,
            BindingResult bindingResult) throws ValidationException, EntityAlreadyExistsException, EntityInvalidArgumentException, EntityNotFoundException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException("BlockedSlot", "Invalid blocked slot inputs", bindingResult);
        }

        BlockedSlotReadOnlyDTO blockedSlot = blockedSlotService.saveBlockedSlot(blockedSlotInsertDto);
        URI location = URI.create("/api/v1/blocked-slots/" + blockedSlot.id());
        return ResponseEntity.created(location).body(blockedSlot);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<BlockedSlotReadOnlyDTO>> getBlockedSlotsByPropertyId(@PathVariable Long propertyId) throws EntityNotFoundException {
        List<BlockedSlotReadOnlyDTO> blockedSlots = blockedSlotService.getBlockedSlotsByPropertyId(propertyId);
        return ResponseEntity.ok(blockedSlots);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BlockedSlotReadOnlyDTO> deleteBlockedSlot(@PathVariable Long id) throws EntityNotFoundException {
        BlockedSlotReadOnlyDTO deletedSlot = blockedSlotService.deleteBlockedSlotById(id);
        return ResponseEntity.ok(deletedSlot);
    }
}
