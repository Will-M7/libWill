package libreria.com.libwill.service;

import libreria.com.libwill.dto.request.RechazarPagoDTO;
import libreria.com.libwill.dto.request.ValidarPagoDTO;
import libreria.com.libwill.dto.response.PagoResponseDTO;
import libreria.com.libwill.entity.enums.EstadoPago;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface PagoService {

    PagoResponseDTO obtenerPagoPorId(Long id);

    List<PagoResponseDTO> obtenerPagosPendientes();

    PagoResponseDTO validarPago(Long id, ValidarPagoDTO dto, String username);

    PagoResponseDTO rechazarPago(Long id, RechazarPagoDTO dto, String username);

    List<PagoResponseDTO> obtenerPagosPorEstado(EstadoPago estado);

    PagoResponseDTO obtenerPagoPorPedidoOnline(Long pedidoId);

    PagoResponseDTO obtenerPagoPorVentaFisica(Long ventaId);

    PagoResponseDTO subirComprobante(Long pedidoId,
                                     MultipartFile archivo,
                                     String numeroOperacion,
                                     String username);

    List<PagoResponseDTO> obtenerPagosPorRangoFechas(Date fechaInicio, Date fechaFin);
}
