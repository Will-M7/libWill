package com.libwill.venta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "sales")
public class SaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String saleCode;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String customerName;

    @NotNull
    @DecimalMin("0.01")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String status;

    @NotBlank
    @Size(max = 30)
    @Column(nullable = false, length = 30)
    private String paymentMethod;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sale_date", nullable = false)
    private Date saleDate;

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
        if (this.saleDate == null) {
            this.saleDate = now;
        }
    }

    @PreUpdate
    private void beforeUpdate() {
        this.updatedAt = new Date();
    }
}
