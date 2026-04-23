package libreria.com.libwill.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import libreria.com.libwill.entity.enums.TipoComprobante;
import libreria.com.libwill.entity.enums.TipoDocumento;
import lombok.Data;

@Data
public class CreateBoletaDTO {

    @NotNull(message = "El tipo de comprobante no puede ser nulo")
    private TipoComprobante tipoComprobante;

    @NotNull(message = "El tipo de documento no puede ser nulo")
    private TipoDocumento tipoDocumento;

    @NotNull(message = "El número de documento no puede ser nulo")
    @Size(min = 8, max = 20, message = "El documento debe tener entre 8 y 20 caracteres")
    private String numeroDocumento;

    @NotNull(message = "El nombre del cliente no puede ser nulo")
    @Size(min = 3, max = 200, message = "El nombre debe tener entre 3 y 200 caracteres")
    private String nombreCliente;

    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    private String direccionCliente;
}