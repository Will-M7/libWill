package libreria.com.libwill.dto.response;

import libreria.com.libwill.entity.enums.EstadoPedido;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class PedidoOnlineResponseDTO {
    private Long id;
    private Date fechaPedido;
    private BigDecimal total;
    private EstadoPedido estado;
    private String direccionEnvio;
    private String numeroCasa;
    private String referencia;
    private String telefonoContacto;
    private String observaciones;
    private UsuarioInfoDTO usuario;
    private List<PedidoItemResponseDTO> items;
    private PagoResponseDTO pago;
    private BoletaResponseDTO boleta;
    private Date fechaCreacion;
    private Date fechaModificacion;
}