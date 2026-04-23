package libreria.com.libwill.dto.response;

import libreria.com.libwill.entity.enums.TipoMovimiento;
import lombok.Data;

import java.util.Date;

@Data
public class MovimientoStockResponseDTO {
    private Long id;
    private ProductoInfoDTO producto;
    private TipoMovimiento tipoMovimiento;
    private Integer cantidad;
    private Integer stockAnterior;
    private Integer stockNuevo;
    private String motivo;
    private UsuarioInfoDTO usuario;
    private Date fechaMovimiento;
}