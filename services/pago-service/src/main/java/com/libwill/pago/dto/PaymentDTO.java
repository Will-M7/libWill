package com.libwill.pago.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PaymentDTO {

    private Long id;

    @NotBlank(message = "El codigo de pago es obligatorio")
    @Size(max = 50, message = "El codigo de pago no puede exceder 50 caracteres")
    private String paymentCode;

    @NotBlank(message = "El codigo de venta es obligatorio")
    @Size(max = 50, message = "El codigo de venta no puede exceder 50 caracteres")
    private String saleCode;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal amount;

    @NotBlank(message = "El metodo de pago es obligatorio")
    @Size(max = 30, message = "El metodo de pago no puede exceder 30 caracteres")
    private String paymentMethod;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 20, message = "El estado no puede exceder 20 caracteres")
    private String status;

    private Date paidAt;
    private Date createdAt;
    private Date updatedAt;
}
