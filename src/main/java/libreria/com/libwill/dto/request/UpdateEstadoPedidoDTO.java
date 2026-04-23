package libreria.com.libwill.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import libreria.com.libwill.entity.enums.EstadoPedido;
import lombok.Data;

@Data
public class UpdateEstadoPedidoDTO {

    @NotNull(message = "El estado no puede ser nulo")
    private EstadoPedido estado;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;
}