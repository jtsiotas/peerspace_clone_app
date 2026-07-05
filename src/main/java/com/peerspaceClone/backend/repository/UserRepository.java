package com.peerspaceClone.backend.repository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.peerspaceClone.backend.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    //added @EntityGraph in order to avoid LazyInitializationException when getting the authorities of a user
    @EntityGraph(attributePaths = {"roles", "roles.capabilities"})
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
