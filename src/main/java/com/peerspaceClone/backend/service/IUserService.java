package com.peerspaceClone.backend.service;

import java.util.UUID;

import com.peerspaceClone.backend.dto.UserInsertDTO;
import com.peerspaceClone.backend.dto.UserReadOnlyDTO;
import com.peerspaceClone.backend.dto.UserUpdateDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;

public interface IUserService {
    public UserReadOnlyDTO saveUser(UserInsertDTO userInsertDto) throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    UserReadOnlyDTO getUserByUUID(UUID uuid) throws EntityNotFoundException;
    UserReadOnlyDTO getUserByUUIDDeletedFalse(UUID uuid) throws EntityNotFoundException;
    UserReadOnlyDTO getUserByEmail(String email) throws EntityNotFoundException;
    UserReadOnlyDTO getUserByUsernameOrEmail(String identifier) throws EntityNotFoundException;
    UserReadOnlyDTO updateUser(UUID uuid, UserUpdateDTO userUpdateDTO) throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException;
    UserReadOnlyDTO deleteUserByUUID(UUID uuid) throws EntityNotFoundException;
}

