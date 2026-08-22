package com.peerspaceClone.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peerspaceClone.backend.dto.MessageInsertDTO;
import com.peerspaceClone.backend.dto.MessageReadOnlyDTO;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;
import com.peerspaceClone.backend.mapper.Mapper;
import com.peerspaceClone.backend.model.Booking;
import com.peerspaceClone.backend.model.Message;
import com.peerspaceClone.backend.model.User;
import com.peerspaceClone.backend.repository.BookingRepository;
import com.peerspaceClone.backend.repository.MessageRepository;
import com.peerspaceClone.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService implements IMessageService {

    private final MessageRepository messageRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityInvalidArgumentException.class})
    public MessageReadOnlyDTO saveMessage(MessageInsertDTO dto) throws EntityNotFoundException, EntityInvalidArgumentException {
        try {
            log.info("Sending message for booking ID: {} by sender ID: {}", dto.bookingId(), dto.senderId());

            Booking booking = bookingRepository.findById(dto.bookingId())
                    .orElseThrow(() -> new EntityNotFoundException("Booking", "Booking with ID=" + dto.bookingId() + " not found"));

            User sender = userRepository.findById(dto.senderId())
                    .orElseThrow(() -> new EntityNotFoundException("User", "Sender with ID=" + dto.senderId() + " not found"));

            // Validate that sender is either guest or host
            boolean isGuest = booking.getGuest().getId().equals(dto.senderId());
            boolean isHost = booking.getProperty().getHost().getId().equals(dto.senderId());

            if (!isGuest && !isHost) {
                throw new EntityInvalidArgumentException("Message", "User with ID=" + dto.senderId() + " is not authorized to send messages to this thread");
            }

            Message message = mapper.mapToMessageEntity(dto);
            message.setBooking(booking);
            message.setSender(sender);

            messageRepository.save(message);
            log.info("Message sent successfully with ID: {}", message.getId());

            return mapper.mapToMessageReadOnlyDTO(message);

        } catch (EntityNotFoundException | EntityInvalidArgumentException e) {
            log.error("Failed to send message: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to send message due to unexpected error", e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageReadOnlyDTO> getMessagesByBookingId(Long bookingId) throws EntityNotFoundException {
        try {
            log.info("Fetching messages for booking ID: {}", bookingId);
            if (!bookingRepository.existsById(bookingId)) {
                throw new EntityNotFoundException("Booking", "Booking with ID=" + bookingId + " not found");
            }

            List<Message> messages = messageRepository.findByBookingIdAndDeletedFalseOrderByCreatedAtAsc(bookingId);
            return messages.stream()
                    .map(mapper::mapToMessageReadOnlyDTO)
                    .collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            log.error("Failed to fetch messages: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error fetching messages for booking ID: {}", bookingId, e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public MessageReadOnlyDTO deleteMessageById(Long id) throws EntityNotFoundException {
        try {
            log.info("Soft deleting message with ID: {}", id);
            Message message = messageRepository.findById(id)
                    .filter(m -> !m.isDeleted())
                    .orElseThrow(() -> new EntityNotFoundException("Message", "Active Message with ID=" + id + " not found"));

            message.softDelete();
            messageRepository.save(message);
            log.info("Message with ID: {} soft deleted successfully", id);

            return mapper.mapToMessageReadOnlyDTO(message);
        } catch (EntityNotFoundException e) {
            log.error("Failed to delete message: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error deleting message with ID: {}", id, e);
            throw e;
        }
    }
}
