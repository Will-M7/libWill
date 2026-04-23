package libreria.com.libwill.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteVentasDTO {
    private Long totalVentas;
    private BigDecimal montoTotalOnline;
    private BigDecimal montoTotalFisica;
    private BigDecimal montoTotalGeneral;
    private Long totalPedidosOnline;
    private Long totalVentasFisicas;
    private Long pedidosPendientes;
    private Long pedidosPagados;
    private Long pedidosEnviados;
    private Long pedidosRecibidos;
}