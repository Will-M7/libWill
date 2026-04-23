package libreria.com.libwill.dto.response;

import libreria.com.libwill.entity.enums.EstadoPago;
import libreria.com.libwill.entity.enums.MetodoPago;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PagoResponseDTO {
    private Long id;
    private MetodoPago metodoPago;
    private BigDecimal monto;
    private String numeroOperacion;
    private EstadoPago estado;
    private Date fechaPago;
    private Date fechaValidacion;
    private UsuarioInfoDTO validadoPor;
    private String comprobanteUrl;
    private String observaciones;
    private String motivoRechazo;
}