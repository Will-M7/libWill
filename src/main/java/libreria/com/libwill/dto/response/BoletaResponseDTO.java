package libreria.com.libwill.dto.response;

import libreria.com.libwill.entity.enums.TipoComprobante;
import libreria.com.libwill.entity.enums.TipoDocumento;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BoletaResponseDTO {
    private Long id;
    private TipoComprobante tipoComprobante;
    private String serie;
    private String numero;
    private String numeroCompleto;
    private Date fechaEmision;
    private TipoDocumento tipoDocumentoCliente;
    private String numeroDocumento;
    private String nombreCliente;
    private String direccionCliente;
    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;
    private String urlPdf;
}