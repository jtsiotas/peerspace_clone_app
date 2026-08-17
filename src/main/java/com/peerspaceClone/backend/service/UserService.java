package com.peerspaceClone.backend.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peerspaceClone.backend.dto.UserInsertDTO;
import com.peerspaceClone.backend.dto.UserReadOnlyDTO;
import com.peerspaceClone.backend.dto.UserUpdateDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;
import com.peerspaceClone.backend.mapper.Mapper;
import com.peerspaceClone.backend.model.Role;
import com.peerspaceClone.backend.model.User;
import com.peerspaceClone.backend.repository.RoleRepository;
import com.peerspaceClone.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements IUserService {
    
    private final UserRepository userRepository;
    private final Mapper mapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityInvalidArgumentException.class})
    public UserReadOnlyDTO saveUser(UserInsertDTO userInsertDto) throws EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            if (userRepository.findByUsername(userInsertDto.username()).isPresent()) {
                throw new EntityAlreadyExistsException("User", "User with username " + userInsertDto.username() + " already exists");
            }
            if (userRepository.existsByEmail(userInsertDto.email())) {
                throw new EntityAlreadyExistsException("User", "User with email " + userInsertDto.email() + " already exists");
            }

            User user = mapper.mapToUserEntity(userInsertDto);
            user.setPassword(passwordEncoder.encode(userInsertDto.password()));

            if (userInsertDto.roleIds() != null) {
                for (Long roleId : userInsertDto.roleIds()) {
                    roleRepository.findById(roleId).ifPresent(user::addRole);
                }
            }

            userRepository.save(user);
            log.info("User with username " + userInsertDto.username() + " saved successfully");
            return mapper.mapToUserReadOnlyDTO(user);

        } catch (EntityAlreadyExistsException e) {
            log.error("Save failed. User with username " + userInsertDto.username() + " already exists");
            throw e;
        } catch (Exception e) {
            log.error("Save failed. Unexpected error");
            throw e;
        }       
    }
    @Override
    //to readOnly veltistopoiei tin taxutita tou commit stin vasi
    @Transactional(readOnly = true)
    public UserReadOnlyDTO getUserByUUID(UUID uuid) throws EntityNotFoundException{
        try{
            User user = userRepository.findByUuid(uuid).orElseThrow(() -> new EntityNotFoundException("User", "User with uuid=" + uuid + " not found"));
            log.debug("User with uuid=" + uuid + " found successfully");
            return mapper.mapToUserReadOnlyDTO(user);
        }catch(EntityNotFoundException e){
            log.error("Get failed. User with uuid=" + uuid + " not found");
            throw e;
        }catch(Exception e){
            log.error("Get failed. Unexpected error");
            throw e;
        }
        
    }
    @Override
    @Transactional(readOnly = true)
    public UserReadOnlyDTO getUserByUUIDDeletedFalse(UUID uuid) throws EntityNotFoundException{
        try{
            User user = userRepository.findByUuidAndDeletedFalse(uuid).orElseThrow(() -> new EntityNotFoundException("User", "User with uuid=" + uuid + " not found"));
            log.debug("User with uuid=" + uuid + " found successfully");
            return mapper.mapToUserReadOnlyDTO(user);
        }catch(EntityNotFoundException e){
            log.error("Get failed. User with uuid=" + uuid + " not found");
            throw e;
        }catch(Exception e){
            log.error("Get failed. Unexpected error");
            throw e;
        }
        
    }

    @Override
    //to readOnly veltistopoiei tin taxutita tou commit stin vasi
    @Transactional(readOnly = true)
    public UserReadOnlyDTO getUserByEmail(String email) throws EntityNotFoundException{
        try{
            User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User", "User with email=" + email + " not found"));
            log.debug("User with email=" + email + " found successfully");
            return mapper.mapToUserReadOnlyDTO(user);
        }catch(EntityNotFoundException e){
            log.error("Get failed. User with email=" + email + " not found");
            throw e;
        }catch(Exception e){
            log.error("Get failed. Unexpected error");
            throw e;
        }
        
    }

    @Override
    @Transactional(readOnly = true)
    public UserReadOnlyDTO getUserByUsernameOrEmail(String identifier) throws EntityNotFoundException {
        try {
            User user = userRepository.findByUsernameOrEmail(identifier, identifier)
                    .orElseThrow(() -> new EntityNotFoundException("User", "User with username or email=" + identifier + " not found"));
            log.debug("User with username/email=" + identifier + " found successfully");
            return mapper.mapToUserReadOnlyDTO(user);
        } catch (EntityNotFoundException e) {
            log.error("Get failed. User with username or email=" + identifier + " not found");
            throw e;
        } catch(Exception e) {
            log.error("Get failed. Unexpected error");
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class, EntityInvalidArgumentException.class})
    public UserReadOnlyDTO updateUser(UUID uuid, UserUpdateDTO userUpdateDTO)
            throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            User existingUser = userRepository.findByUuid(uuid)
                    .orElseThrow(() -> new EntityNotFoundException("User", "User with uuid=" + uuid + " not found"));

            // Check if updated username belongs to another user
            Optional<User> userWithSameUsername = userRepository.findByUsername(userUpdateDTO.username());
            if (userWithSameUsername.isPresent() && !userWithSameUsername.get().getId().equals(existingUser.getId())) {
                throw new EntityAlreadyExistsException("User", "User with username " + userUpdateDTO.username() + " already exists");
            }

            // Check if updated email belongs to another user
            Optional<User> userWithSameEmail = userRepository.findByEmail(userUpdateDTO.email());
            if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(existingUser.getId())) {
                throw new EntityAlreadyExistsException("User", "User with email " + userUpdateDTO.email() + " already exists");
            }

            mapper.updateUserEntity(existingUser, userUpdateDTO);
            userRepository.save(existingUser);
            log.info("User with uuid=" + uuid + " updated successfully");

            return mapper.mapToUserReadOnlyDTO(existingUser);

        } catch (EntityNotFoundException e) {
            log.error("Update failed. User with uuid=" + uuid + " not found");
            throw e;
        } catch (EntityAlreadyExistsException e) {
            log.error("Update failed. Duplicate data for user update with uuid=" + uuid);
            throw e;
        } catch (Exception e) {
            log.error("Update failed. Unexpected error during update for user with uuid=" + uuid);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public UserReadOnlyDTO deleteUserByUUID(UUID uuid) throws EntityNotFoundException {
        try {
            User user = userRepository.findByUuidAndDeletedFalse(uuid)
                    .orElseThrow(() -> new EntityNotFoundException("User", "User with uuid=" + uuid + " not found or already deleted"));

            user.softDelete();
            userRepository.save(user);
            log.info("User with uuid=" + uuid + " soft-deleted successfully");

            return mapper.mapToUserReadOnlyDTO(user);

        } catch (EntityNotFoundException e) {
            log.error("Delete failed. User with uuid=" + uuid + " not found or already deleted");
            throw e;
        } catch (Exception e) {
            log.error("Delete failed. Unexpected error during deletion for user with uuid=" + uuid);
            throw e;
        }
    }
}