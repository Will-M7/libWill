package com.libwill.pago.entity;

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
@Table(name = "payments")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String paymentCode;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String saleCode;

    @NotNull
    @DecimalMin("0.01")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @NotBlank
    @Size(max = 30)
    @Column(nullable = false, length = 30)
    private String paymentMethod;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "paid_at", nullable = false)
    private Date paidAt;

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
        if (this.paidAt == null) {
            this.paidAt = now;
        }
    }

    @PreUpdate
    private void beforeUpdate() {
        this.updatedAt = new Date();
    }
}
