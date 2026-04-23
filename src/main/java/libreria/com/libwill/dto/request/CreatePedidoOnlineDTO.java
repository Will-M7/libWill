package libreria.com.libwill.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreatePedidoOnlineDTO {

    @NotNull(message = "La dirección de envío no puede ser nula")
    @Size(min = 10, max = 255, message = "La dirección debe tener entre 10 y 255 caracteres")
    private String direccionEnvio;

    @Size(max = 20, message = "El número de casa no puede exceder 20 caracteres")
    private String numeroCasa;

    @Size(max = 255, message = "La referencia no puede exceder 255 caracteres")
    private String referencia;

    @Size(max = 15, message = "El teléfono no puede exceder 15 caracteres")
    private String telefonoContacto;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;

    @NotEmpty(message = "El pedido debe tener al menos un producto")
    @Valid
    private List<CreatePedidoItemDTO> items;
}