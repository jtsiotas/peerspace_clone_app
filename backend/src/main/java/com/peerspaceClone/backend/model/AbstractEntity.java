package com.peerspaceClone.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {

    @CreatedDate
    //columnDefinition = "TIMESTAMP WITH TIME ZONE" αυτό το αφαίερεσα και αφήνω το Hibernate να το βάλει αυτόματα αν έπρεπε να βάλω κάτι θα ήταν columnDefinition = "TIMESTAMP WITH TIME ZONE"
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdAt;              // UTC

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant updatedAt;

    @Column(nullable = false)
    private boolean deleted;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant deletedAt;

    public void softDelete() {
        this.deleted = true;
        this.deletedAt = Instant.now();
    }
}
