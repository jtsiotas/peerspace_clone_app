package com.peerspaceClone.backend.service;

import java.util.List;
import com.peerspaceClone.backend.dto.MessageInsertDTO;
import com.peerspaceClone.backend.dto.MessageReadOnlyDTO;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;

public interface IMessageService {
    MessageReadOnlyDTO saveMessage(MessageInsertDTO dto) throws EntityNotFoundException, EntityInvalidArgumentException;
    List<MessageReadOnlyDTO> getMessagesByBookingId(Long bookingId) throws EntityNotFoundException;
    MessageReadOnlyDTO deleteMessageById(Long id) throws EntityNotFoundException;
}
