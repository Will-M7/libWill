package libreria.com.libwill.service.impl;

import libreria.com.libwill.dto.request.CreatePedidoItemDTO;
import libreria.com.libwill.dto.request.CreatePedidoOnlineDTO;
import libreria.com.libwill.dto.request.UpdateEstadoPedidoDTO;
import libreria.com.libwill.dto.response.PedidoOnlineResponseDTO;
import libreria.com.libwill.entity.*;
import libreria.com.libwill.entity.enums.*;
import libreria.com.libwill.mapper.PedidoOnlineMapper;
import libreria.com.libwill.repository.*;
import libreria.com.libwill.service.PedidoOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PedidoOnlineServiceImpl implements PedidoOnlineService {

    @Autowired
    private PedidoOnlineRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private MovimientoStockRepository movimientoStockRepository;

    @Autowired
    private PedidoOnlineMapper mapper;

    private static final int CANTIDAD_MAYORISTA = 12;

    @Override
    @Transactional
    public PedidoOnlineResponseDTO crearPedido(CreatePedidoOnlineDTO dto, String username) {
        // 1. Buscar usuario
        UsuarioEntity usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Validar que todos los productos existan y tengan stock
        for (CreatePedidoItemDTO itemDTO : dto.getItems()) {
            ProductEntity producto = productRepository.findById(itemDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + itemDTO.getProductoId()));

            if (!producto.tieneStock(itemDTO.getCantidad())) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getName() +
                        ". Disponible: " + producto.getStock() + ", Solicitado: " + itemDTO.getCantidad());
            }
        }

        // 3. Crear pedido
        PedidoOnlineEntity pedido = new PedidoOnlineEntity();
        pedido.setUsuario(usuario);
        pedido.setDireccionEnvio(dto.getDireccionEnvio());
        pedido.setNumeroCasa(dto.getNumeroCasa());
        pedido.setReferencia(dto.getReferencia());
        pedido.setTelefonoContacto(dto.getTelefonoContacto());
        pedido.setObservaciones(dto.getObservaciones());
        pedido.setEstado(EstadoPedido.PENDIENTE_PAGO);

        // 4. Agregar items
        BigDecimal total = BigDecimal.ZERO;
        for (CreatePedidoItemDTO itemDTO : dto.getItems()) {
            ProductEntity producto = productRepository.findById(itemDTO.getProductoId()).get();

            PedidoItemEntity item = new PedidoItemEntity();
            item.setProducto(producto);
            item.setCantidad(itemDTO.getCantidad());

            // Determinar precio (mayorista o minorista)
            if (itemDTO.getCantidad() >= CANTIDAD_MAYORISTA) {
                item.setPrecioUnitario(producto.getPriceMajor());
                item.setTipoPrecio(TipoPrecio.MAYORISTA);
            } else {
                item.setPrecioUnitario(producto.getPriceMinor());
                item.setTipoPrecio(TipoPrecio.MINORISTA);
            }

            item.setNombreProducto(producto.getName());
            item.setPresentacion(producto.getPresentation());

            BigDecimal subtotal = item.getPrecioUnitario().multiply(BigDecimal.valueOf(itemDTO.getCantidad()));
            item.setSubtotal(subtotal);
            total = total.add(subtotal);

            pedido.addItem(item);
        }

        pedido.setTotal(total);

        // 5. Crear pago pendiente
        PagoEntity pago = new PagoEntity();
        pago.setPedidoOnline(pedido);
        pago.setMetodoPago(MetodoPago.YAPE);
        pago.setMonto(total);
        pago.setEstado(EstadoPago.PENDIENTE);
        pedido.setPago(pago);

        // 6. Guardar
        PedidoOnlineEntity pedidoGuardado = pedidoRepository.save(pedido);

        return mapper.toResponseDTO(pedidoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoOnlineResponseDTO> obtenerMisPedidos(String username) {
        List<PedidoOnlineEntity> pedidos = pedidoRepository.findByUsername(username);
        return mapper.toResponseDTOList(pedidos);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoOnlineResponseDTO obtenerPedidoPorId(Long id, String username) {
        PedidoOnlineEntity pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Verificar permisos
        UsuarioEntity usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isAdmin = usuario.getRoles().stream()
                .anyMatch(rol -> rol.getNombre().equals("ROLE_ADMIN") || rol.getNombre().equals("ROLE_VENDEDOR"));

        if (!isAdmin && !pedido.getUsuario().getUsername().equals(username)) {
            throw new AccessDeniedException("No tienes permiso para ver este pedido");
        }

        return mapper.toResponseDTO(pedido);
    }

    @Override
    @Transactional
    public PedidoOnlineResponseDTO actualizarEstado(Long id, UpdateEstadoPedidoDTO dto, String username) {
        PedidoOnlineEntity pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Si se cambia a PAGADO, descontar stock y generar boleta
        if (dto.getEstado() == EstadoPedido.PAGADO && pedido.getEstado() != EstadoPedido.PAGADO) {
            descontarStockPedido(pedido, username);
        }

        pedido.setEstado(dto.getEstado());
        if (dto.getObservaciones() != null) {
            pedido.setObservaciones(dto.getObservaciones());
        }
        pedido.setModificadoPor(username);

        PedidoOnlineEntity pedidoActualizado = pedidoRepository.save(pedido);
        return mapper.toResponseDTO(pedidoActualizado);
    }

    @Override
    @Transactional
    public PedidoOnlineResponseDTO cancelarPedido(Long id, String username) {
        PedidoOnlineEntity pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!pedido.getUsuario().getUsername().equals(username)) {
            throw new AccessDeniedException("No tienes permiso para cancelar este pedido");
        }

        if (pedido.getEstado() != EstadoPedido.PENDIENTE_PAGO) {
            throw new RuntimeException("Solo puedes cancelar pedidos pendientes de pago");
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        pedido.setModificadoPor(username);

        PedidoOnlineEntity pedidoActualizado = pedidoRepository.save(pedido);
        return mapper.toResponseDTO(pedidoActualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoOnlineResponseDTO> obtenerTodosPedidos() {
        List<PedidoOnlineEntity> pedidos = pedidoRepository.findAll();
        return mapper.toResponseDTOList(pedidos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoOnlineResponseDTO> obtenerPedidosPorEstado(EstadoPedido estado) {
        List<PedidoOnlineEntity> pedidos = pedidoRepository.findByEstado(estado);
        return mapper.toResponseDTOList(pedidos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoOnlineResponseDTO> obtenerPedidosPorFecha(Date fechaInicio, Date fechaFin) {
        List<PedidoOnlineEntity> pedidos = pedidoRepository.findByFechaBetween(fechaInicio, fechaFin);
        return mapper.toResponseDTOList(pedidos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoOnlineResponseDTO> obtenerPedidosPendientesPago() {
        List<PedidoOnlineEntity> pedidos = pedidoRepository.findPendientesPago();
        return mapper.toResponseDTOList(pedidos);
    }

    // Método auxiliar: Descontar stock del pedido
    private void descontarStockPedido(PedidoOnlineEntity pedido, String username) {
        UsuarioEntity usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        for (PedidoItemEntity item : pedido.getItems()) {
            ProductEntity producto = item.getProducto();
            int stockAnterior = producto.getStock();

            // Descontar stock
            producto.descontarStock(item.getCantidad());
            productRepository.save(producto);

            // Registrar movimiento
            MovimientoStockEntity movimiento = new MovimientoStockEntity();
            movimiento.setProducto(producto);
            movimiento.setTipoMovimiento(TipoMovimiento.VENTA_ONLINE);
            movimiento.setCantidad(-item.getCantidad());
            movimiento.setStockAnterior(stockAnterior);
            movimiento.setStockNuevo(producto.getStock());
            movimiento.setMotivo("Venta online - Pedido #" + pedido.getId());
            movimiento.setUsuario(usuario);
            movimiento.setPedidoOnline(pedido);
            movimientoStockRepository.save(movimiento);
        }
    }
}