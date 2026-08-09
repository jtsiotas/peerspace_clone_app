package com.peerspaceClone.backend.api;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peerspaceClone.backend.dto.UserInsertDTO;
import com.peerspaceClone.backend.dto.UserReadOnlyDTO;
import com.peerspaceClone.backend.dto.UserUpdateDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;
import com.peerspaceClone.backend.core.exception.ValidationException;
import com.peerspaceClone.backend.service.IUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final IUserService userService;

    @PostMapping
    public ResponseEntity<UserReadOnlyDTO> registerUser(
            @Valid @RequestBody UserInsertDTO userInsertDto, 
            BindingResult bindingResult) throws ValidationException, EntityAlreadyExistsException, EntityInvalidArgumentException {
        
        if (bindingResult.hasErrors()) {
            throw new ValidationException("User", "Invalid user data", bindingResult);
        }

        UserReadOnlyDTO userReadOnlyDTO = userService.saveUser(userInsertDto);
        URI location = URI.create("/api/v1/users/" + userReadOnlyDTO.getUuid());
        return ResponseEntity.created(location).body(userReadOnlyDTO);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserReadOnlyDTO> getUserByUuid(@PathVariable UUID uuid) throws EntityNotFoundException {
        UserReadOnlyDTO user = userService.getUserByUUIDDeletedFalse(uuid);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<UserReadOnlyDTO> updateUser(
            @PathVariable UUID uuid,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO,
            BindingResult bindingResult) throws ValidationException, EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException {
        
        if (bindingResult.hasErrors()) {
            throw new ValidationException("User", "Invalid user update data", bindingResult);
        }

        UserReadOnlyDTO updatedUser = userService.updateUser(uuid, userUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<UserReadOnlyDTO> deleteUser(@PathVariable UUID uuid) throws EntityNotFoundException {
        UserReadOnlyDTO deletedUser = userService.deleteUserByUUID(uuid);
        return ResponseEntity.ok(deletedUser);
    }
}