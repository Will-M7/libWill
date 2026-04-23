package libreria.com.libwill.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import libreria.com.libwill.entity.enums.MetodoPago;
import libreria.com.libwill.entity.enums.TipoVenta;
import lombok.Data;

import java.util.List;

@Data
public class CreateVentaFisicaDTO {

    @NotNull(message = "El tipo de venta no puede ser nulo")
    private TipoVenta tipoVenta;

    @NotNull(message = "El método de pago no puede ser nulo")
    private MetodoPago metodoPago;

    @Size(max = 100, message = "El número de operación no puede exceder 100 caracteres")
    private String numeroOperacion; // Para Yape

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;

    @NotEmpty(message = "La venta debe tener al menos un producto")
    @Valid
    private List<CreateVentaItemDTO> items;

    // 📋 Datos para boleta (solo si tipoVenta = CON_BOLETA)
    private CreateBoletaDTO datosCliente;
}