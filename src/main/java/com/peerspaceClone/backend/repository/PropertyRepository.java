package com.peerspaceClone.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.EntityGraph;
import com.peerspaceClone.backend.model.Property;
import com.peerspaceClone.backend.model.User;

public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {
    List<Property> findByHost(User host);
    List<Property> findByHostId(Long hostId);
    List<Property> findByHostUsername(String username);
    //Adding @EntityGraph in order to fetch the host with the property and not in separate queries (N+1 problem)
    @EntityGraph(attributePaths = {"host"})
    Page<Property> findAllByDeletedFalse(Pageable pageable);
    //The same as above
    @EntityGraph(attributePaths = {"host"})
    Page<Property> findByCity(String city, Pageable pageable);

}