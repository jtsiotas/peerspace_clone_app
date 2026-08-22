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
    
    @EntityGraph(attributePaths = {"host"})
    List<Property> findByHostId(Long hostId);

    @EntityGraph(attributePaths = {"host"})
    List<Property> findByHostIdAndDeletedFalse(Long hostId);

    List<Property> findByHostUsername(String username);

    @Override
    @EntityGraph(attributePaths = {"host"})
    Optional<Property> findById(Long id);

    @EntityGraph(attributePaths = {"host"})
    Optional<Property> findByIdAndDeletedFalse(Long id);

    Optional<Property> findByTitle(String title);
    boolean existsByTitle(String title);
    boolean existsByAddress(String address);

    //Adding @EntityGraph in order to fetch the host with the property and not in separate queries (N+1 problem)
    @EntityGraph(attributePaths = {"host"})
    Page<Property> findAllByDeletedFalse(Pageable pageable);

    @EntityGraph(attributePaths = {"host"})
    List<Property> findAllByDeletedFalse();

    //The same as above
    @EntityGraph(attributePaths = {"host"})
    Page<Property> findByCity(String city, Pageable pageable);
}