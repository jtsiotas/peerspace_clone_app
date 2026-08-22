package com.peerspaceClone.backend.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peerspaceClone.backend.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAllByOrderByNameAsc();
    
}