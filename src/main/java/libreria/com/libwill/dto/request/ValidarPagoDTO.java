package libreria.com.libwill.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ValidarPagoDTO {

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;
}