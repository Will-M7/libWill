package libreria.com.libwill.service;

import libreria.com.libwill.dto.request.CreatePedidoOnlineDTO;
import libreria.com.libwill.dto.request.UpdateEstadoPedidoDTO;
import libreria.com.libwill.dto.response.PedidoOnlineResponseDTO;
import libreria.com.libwill.entity.enums.EstadoPedido;

import java.util.Date;
import java.util.List;

public interface PedidoOnlineService {

    // Crear pedido (cliente)
    PedidoOnlineResponseDTO crearPedido(CreatePedidoOnlineDTO dto, String username);

    // Obtener mis pedidos (cliente)
    List<PedidoOnlineResponseDTO> obtenerMisPedidos(String username);

    // Obtener pedido por ID
    PedidoOnlineResponseDTO obtenerPedidoPorId(Long id, String username);

    // Actualizar estado (admin/vendedor)
    PedidoOnlineResponseDTO actualizarEstado(Long id, UpdateEstadoPedidoDTO dto, String username);

    // Cancelar pedido (cliente - solo si está PENDIENTE_PAGO)
    PedidoOnlineResponseDTO cancelarPedido(Long id, String username);

    // Obtener todos los pedidos (admin)
    List<PedidoOnlineResponseDTO> obtenerTodosPedidos();

    // Obtener pedidos por estado (admin/vendedor)
    List<PedidoOnlineResponseDTO> obtenerPedidosPorEstado(EstadoPedido estado);

    // Obtener pedidos por rango de fechas (admin)
    List<PedidoOnlineResponseDTO> obtenerPedidosPorFecha(Date fechaInicio, Date fechaFin);

    // Obtener pedidos pendientes de pago
    List<PedidoOnlineResponseDTO> obtenerPedidosPendientesPago();
}