package com.libwill.venta.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SaleDTO {

    private Long id;

    @NotBlank(message = "El codigo de venta es obligatorio")
    @Size(max = 50, message = "El codigo de venta no puede exceder 50 caracteres")
    private String saleCode;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 150, message = "El nombre del cliente no puede exceder 150 caracteres")
    private String customerName;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.01", message = "El total debe ser mayor a 0")
    private BigDecimal totalAmount;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 20, message = "El estado no puede exceder 20 caracteres")
    private String status;

    @NotBlank(message = "El metodo de pago es obligatorio")
    @Size(max = 30, message = "El metodo de pago no puede exceder 30 caracteres")
    private String paymentMethod;

    private Date saleDate;
    private Date createdAt;
    private Date updatedAt;
}
