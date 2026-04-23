package libreria.com.libwill.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RechazarPagoDTO {

    @NotNull(message = "El motivo de rechazo no puede ser nulo")
    @Size(min = 10, max = 500, message = "El motivo debe tener entre 10 y 500 caracteres")
    private String motivoRechazo;
}