package com.libwill.inventario.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "inventory_movements")
public class InventoryMovementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String productSku;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String movementType;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer quantity;

    @Size(max = 60)
    @Column(length = 60)
    private String referenceCode;

    @Size(max = 300)
    @Column(length = 300)
    private String note;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "movement_date", nullable = false)
    private Date movementDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @PrePersist
    private void beforePersist() {
        Date now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.movementDate == null) {
            this.movementDate = now;
        }
    }

    @PreUpdate
    private void beforeUpdate() {
        this.updatedAt = new Date();
    }
}
