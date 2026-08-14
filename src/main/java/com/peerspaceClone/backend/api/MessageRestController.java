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

import com.peerspaceClone.backend.dto.MessageInsertDTO;
import com.peerspaceClone.backend.dto.MessageReadOnlyDTO;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;
import com.peerspaceClone.backend.core.exception.ValidationException;
import com.peerspaceClone.backend.service.IMessageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageRestController {

    private final IMessageService messageService;

    @PostMapping
    public ResponseEntity<MessageReadOnlyDTO> sendMessage(
            @Valid @RequestBody MessageInsertDTO messageInsertDto,
            BindingResult bindingResult) throws ValidationException, EntityNotFoundException, EntityInvalidArgumentException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException("Message", "Invalid message details provided", bindingResult);
        }

        MessageReadOnlyDTO message = messageService.saveMessage(messageInsertDto);
        URI location = URI.create("/api/v1/messages/" + message.id());
        return ResponseEntity.created(location).body(message);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<MessageReadOnlyDTO>> getMessagesByBookingId(@PathVariable Long bookingId) throws EntityNotFoundException {
        List<MessageReadOnlyDTO> messages = messageService.getMessagesByBookingId(bookingId);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageReadOnlyDTO> deleteMessage(@PathVariable Long id) throws EntityNotFoundException {
        MessageReadOnlyDTO deletedMessage = messageService.deleteMessageById(id);
        return ResponseEntity.ok(deletedMessage);
    }
}
