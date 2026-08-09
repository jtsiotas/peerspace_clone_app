package com.peerspaceClone.backend.mapper;

import org.springframework.stereotype.Component;
import com.peerspaceClone.backend.dto.UserInsertDTO;
import com.peerspaceClone.backend.dto.UserReadOnlyDTO;
import com.peerspaceClone.backend.dto.UserUpdateDTO;
import com.peerspaceClone.backend.model.User;

@Component
public class Mapper {

    public User mapToUserEntity(UserInsertDTO userInsertDto) {
        User user = new User();
        user.setUsername(userInsertDto.username());
        user.setEmail(userInsertDto.email());
        user.setFirstname(userInsertDto.firstName());
        user.setLastname(userInsertDto.lastName());
        return user;
    }
    
    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(
            user.getId(), 
            user.getUuid(),
            user.getUsername(), 
            user.getEmail(), 
            user.getFirstname(), 
            user.getLastname(), 
            user.getAllRoles()
        );
    }

    public void updateUserEntity(User user, UserUpdateDTO dto) {
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setFirstname(dto.firstName());
        user.setLastname(dto.lastName());
    }
}