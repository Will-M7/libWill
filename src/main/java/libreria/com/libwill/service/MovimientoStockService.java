package libreria.com.libwill.service;

import libreria.com.libwill.dto.request.AgregarStockDTO;
import libreria.com.libwill.dto.response.MovimientoStockResponseDTO;
import libreria.com.libwill.entity.enums.TipoMovimiento;

import java.util.Date;
import java.util.List;

public interface MovimientoStockService {

    // Agregar stock manualmente (admin/vendedor)
    MovimientoStockResponseDTO agregarStock(AgregarStockDTO dto, String username);

    // Obtener movimientos por producto
    List<MovimientoStockResponseDTO> obtenerMovimientosPorProducto(Long productoId);

    // Obtener movimientos por tipo
    List<MovimientoStockResponseDTO> obtenerMovimientosPorTipo(TipoMovimiento tipo);

    // Obtener movimientos por rango de fechas
    List<MovimientoStockResponseDTO> obtenerMovimientosPorFecha(Date fechaInicio, Date fechaFin);

    // Obtener últimos movimientos (auditoría)
    List<MovimientoStockResponseDTO> obtenerUltimosMovimientos();
}