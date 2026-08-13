package com.peerspaceClone.backend.mapper;

import org.springframework.stereotype.Component;
import com.peerspaceClone.backend.dto.UserInsertDTO;
import com.peerspaceClone.backend.dto.UserReadOnlyDTO;
import com.peerspaceClone.backend.dto.UserUpdateDTO;
import com.peerspaceClone.backend.dto.PropertyInsertDTO;
import com.peerspaceClone.backend.dto.PropertyReadOnlyDTO;
import com.peerspaceClone.backend.dto.PropertyUpdateDTO;
import com.peerspaceClone.backend.model.User;
import com.peerspaceClone.backend.model.Property;

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

    public Property mapToPropertyEntity(PropertyInsertDTO dto) {
        Property property = new Property();
        property.setTitle(dto.title());
        property.setDescription(dto.description());
        property.setCity(dto.city());
        property.setAddress(dto.address());
        property.setStatus(dto.status());
        property.setHourlyRate(dto.hourlyRate());
        property.setHalfDayRate(dto.halfDayRate());
        property.setZip(dto.zip());
        property.setTimezone(dto.timezone());
        property.setLongitude(dto.longitude());
        property.setLatitude(dto.latitude());
        property.setSizeSqm(dto.sizeSqm());
        property.setCapacity(dto.capacity());
        property.setMinHours(dto.minHours());
        property.setMaxHours(dto.maxHours());
        property.setType(dto.type());
        return property;
    }

    public PropertyReadOnlyDTO mapToPropertyReadOnlyDTO(Property property) {
        return new PropertyReadOnlyDTO(
            property.getId(),
            property.getHost() != null ? property.getHost().getId() : null,
            property.getTitle(),
            property.getDescription(),
            property.getCity(),
            property.getAddress(),
            property.getStatus(),
            property.getHourlyRate(),
            property.getHalfDayRate(),
            property.getZip(),
            property.getTimezone(),
            property.getLongitude(),
            property.getLatitude(),
            property.getSizeSqm(),
            property.getCapacity(),
            property.getMinHours(),
            property.getMaxHours(),
            property.getType()
        );
    }

    public void updatePropertyEntity(Property property, PropertyUpdateDTO dto) {
        property.setTitle(dto.title());
        property.setDescription(dto.description());
        property.setStatus(dto.status());
        property.setHourlyRate(dto.hourlyRate());
        property.setHalfDayRate(dto.halfDayRate());
        property.setCapacity(dto.capacity());
        property.setMinHours(dto.minHours());
        property.setMaxHours(dto.maxHours());
        property.setType(dto.type());
    }
}