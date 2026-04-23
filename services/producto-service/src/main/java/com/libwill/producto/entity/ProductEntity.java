package com.libwill.producto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.libwill.producto.util.AuditoriaUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre del producto no puede ser nulo")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    @Column(nullable = false, length = 150)
    private String name;

    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    private String description;

    @NotNull(message = "El SKU no puede ser nulo")
    @Column(nullable = false, unique = true, length = 100)
    private String sku;

    @Column(length = 50)
    private String presentation; // Ejemplo: "250g", "1kg"

    @NotNull(message = "El precio minorista no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio minorista debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceMinor;

    @NotNull(message = "El precio mayorista no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio mayorista debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceMajor;

    // ⭐ NUEVO: Stock del producto
    @NotNull(message = "El stock no puede ser nulo")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock = 0;

    @Size(max = 255, message = "La URL de imagen no puede exceder los 255 caracteres")
    private String imageUrl;

    @Column(nullable = false)
    private Boolean active = true;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @Column(name = "created_by", updatable = false, length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @PrePersist
    private void beforePersist() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.createdBy = AuditoriaUtil.obtenerUsuarioActual();
    }

    @PreUpdate
    private void beforeUpdate() {
        this.updatedAt = new Date();
        this.updatedBy = AuditoriaUtil.obtenerUsuarioActual();
    }

    // ⭐ MÉTODO AUXILIAR: Verificar si hay stock disponible
    public boolean tieneStock(int cantidad) {
        return this.stock >= cantidad;
    }

    // ⭐ MÉTODO AUXILIAR: Descontar stock
    public void descontarStock(int cantidad) {
        if (!tieneStock(cantidad)) {
            throw new IllegalStateException("Stock insuficiente para el producto: " + this.name);
        }
        this.stock -= cantidad;
    }

    // ⭐ MÉTODO AUXILIAR: Agregar stock
    public void agregarStock(int cantidad) {
        this.stock += cantidad;
    }

}
