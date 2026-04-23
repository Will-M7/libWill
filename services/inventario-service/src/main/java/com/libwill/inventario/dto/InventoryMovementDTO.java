package com.libwill.inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class InventoryMovementDTO {

    private Long id;

    @NotBlank(message = "El SKU del producto es obligatorio")
    @Size(max = 100, message = "El SKU no puede exceder 100 caracteres")
    private String productSku;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Size(max = 20, message = "El tipo no puede exceder 20 caracteres")
    private String movementType;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor o igual a 1")
    private Integer quantity;

    @Size(max = 60, message = "El codigo de referencia no puede exceder 60 caracteres")
    private String referenceCode;

    @Size(max = 300, message = "La nota no puede exceder 300 caracteres")
    private String note;

    private Date movementDate;
    private Date createdAt;
    private Date updatedAt;
}
