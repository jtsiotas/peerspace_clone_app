package com.peerspaceClone.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
// @AllArgsConstructor
@Getter
@Setter
@Table(name = "property_images")
public class PropertyImage extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    private String url;

    private Boolean isMain;

    private Integer displayOrder;

}
