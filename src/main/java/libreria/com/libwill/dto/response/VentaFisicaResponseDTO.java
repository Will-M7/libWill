package libreria.com.libwill.dto.response;

import libreria.com.libwill.entity.enums.TipoVenta;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class VentaFisicaResponseDTO {
    private Long id;
    private UsuarioInfoDTO vendedor;
    private TipoVenta tipoVenta;
    private Date fechaVenta;
    private BigDecimal total;
    private String estado;
    private String observaciones;
    private List<VentaItemResponseDTO> items;
    private PagoResponseDTO pago;
    private BoletaResponseDTO boleta;
    private Date fechaCreacion;
}