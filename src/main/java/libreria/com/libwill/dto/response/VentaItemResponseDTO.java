package libreria.com.libwill.dto.response;

import libreria.com.libwill.entity.enums.TipoPrecio;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VentaItemResponseDTO {
    private Long id;
    private ProductoInfoDTO producto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private TipoPrecio tipoPrecio;
}