package com.peerspaceClone.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.peerspaceClone.backend.model.BlockedSlot;
import java.time.LocalDateTime;
import java.util.List;

public interface BlockedSlotRepository extends JpaRepository<BlockedSlot, Long> {
    List<BlockedSlot> findByPropertyId(Long propertyId);

    @Query("SELECT COUNT(bs) > 0 FROM BlockedSlot bs WHERE bs.property.id = :propertyId AND bs.startTime < :end AND bs.endTime > :start")
    boolean existsOverlappingBlockedSlot(@Param("propertyId") Long propertyId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
