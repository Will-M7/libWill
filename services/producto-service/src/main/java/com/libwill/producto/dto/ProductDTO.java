package com.libwill.producto.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductDTO {

    private Long id;

    @NotNull(message = "El nombre del producto no puede ser nulo")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String name;

    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    private String description;

    @NotNull(message = "El SKU no puede ser nulo")
    @Size(min = 3, max = 100, message = "El SKU debe tener entre 3 y 100 caracteres")
    private String sku;

    @Size(max = 50, message = "La presentación no puede exceder los 50 caracteres")
    private String presentation;

    @NotNull(message = "El precio minorista no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio minorista debe ser mayor a 0")
    private BigDecimal priceMinor;

    @NotNull(message = "El precio mayorista no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio mayorista debe ser mayor a 0")
    private BigDecimal priceMajor;

    private Integer stock;

    @Size(max = 255, message = "La URL de la imagen no puede exceder los 255 caracteres")
    private String imageUrl;

    private Boolean active;

    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;


}
