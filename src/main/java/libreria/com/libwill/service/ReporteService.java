package libreria.com.libwill.service;

import libreria.com.libwill.dto.response.ReporteVentasDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReporteService {

    // Reporte general de ventas
    ReporteVentasDTO obtenerReporteGeneral(Date fechaInicio, Date fechaFin);

    // Productos más vendidos (online + física)
    List<Map<String, Object>> obtenerProductosMasVendidos(Date fechaInicio, Date fechaFin);

    // Ranking de vendedores
    List<Map<String, Object>> obtenerRankingVendedores(Date fechaInicio, Date fechaFin);

    // Ventas por método de pago
    Map<String, Object> obtenerVentasPorMetodoPago(Date fechaInicio, Date fechaFin);

    // Resumen de inventario actual
    List<Map<String, Object>> obtenerResumenInventario();
}