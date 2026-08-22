package com.peerspaceClone.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peerspaceClone.backend.model.Capability;


public interface CapabilityRepository extends JpaRepository<Capability, Long> {
}
