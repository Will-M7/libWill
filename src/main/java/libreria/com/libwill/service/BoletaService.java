package libreria.com.libwill.service;

import libreria.com.libwill.dto.response.BoletaResponseDTO;
import libreria.com.libwill.entity.enums.TipoComprobante;

import java.util.Date;
import java.util.List;

public interface BoletaService {

    // Obtener boleta por ID
    BoletaResponseDTO obtenerBoletaPorId(Long id);

    // Obtener boleta por pedido online
    BoletaResponseDTO obtenerBoletaPorPedido(Long pedidoId);

    // Obtener boleta por venta física
    BoletaResponseDTO obtenerBoletaPorVenta(Long ventaId);

    // Obtener todas las boletas (admin)
    List<BoletaResponseDTO> obtenerTodasLasBoletas();

    // Obtener boletas por tipo
    List<BoletaResponseDTO> obtenerBoletasPorTipo(TipoComprobante tipo);

    // Obtener boletas por rango de fechas
    List<BoletaResponseDTO> obtenerBoletasPorFecha(Date fechaInicio, Date fechaFin);

    // Descargar boleta en PDF (método auxiliar - se implementará después)
    byte[] generarBoletaPDF(Long boletaId);
}