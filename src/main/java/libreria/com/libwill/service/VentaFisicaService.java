package libreria.com.libwill.service;

import libreria.com.libwill.dto.request.CreateVentaFisicaDTO;
import libreria.com.libwill.dto.response.VentaFisicaResponseDTO;

import java.util.Date;
import java.util.List;

public interface VentaFisicaService {

    // Crear venta física (vendedor)
    VentaFisicaResponseDTO crearVenta(CreateVentaFisicaDTO dto, String username);

    // Obtener mis ventas (vendedor)
    List<VentaFisicaResponseDTO> obtenerMisVentas(String username);

    // Obtener venta por ID
    VentaFisicaResponseDTO obtenerVentaPorId(Long id, String username);

    // Obtener todas las ventas (admin)
    List<VentaFisicaResponseDTO> obtenerTodasLasVentas();

    // Obtener ventas por vendedor (admin)
    List<VentaFisicaResponseDTO> obtenerVentasPorVendedor(Long vendedorId);

    // Obtener ventas por rango de fechas
    List<VentaFisicaResponseDTO> obtenerVentasPorFecha(Date fechaInicio, Date fechaFin);

    // Obtener ventas del día (vendedor)
    List<VentaFisicaResponseDTO> obtenerVentasDelDia(String username);
}