package libreria.com.libwill.service.impl;

import libreria.com.libwill.dto.request.RechazarPagoDTO;
import libreria.com.libwill.dto.request.ValidarPagoDTO;
import libreria.com.libwill.dto.response.PagoResponseDTO;
import libreria.com.libwill.entity.*;
import libreria.com.libwill.entity.enums.EstadoPago;
import libreria.com.libwill.entity.enums.EstadoPedido;
import libreria.com.libwill.entity.enums.MetodoPago;
import libreria.com.libwill.entity.enums.TipoMovimiento;
import libreria.com.libwill.mapper.PagoMapper;
import libreria.com.libwill.repository.*;
import libreria.com.libwill.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoOnlineRepository pedidoRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MovimientoStockRepository movimientoStockRepository;

    @Autowired
    private PagoMapper mapper;

    // ========= CONSULTAS =========

    @Override
    @Transactional(readOnly = true)
    public PagoResponseDTO obtenerPagoPorId(Long id) {
        PagoEntity pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return mapper.toResponseDTO(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponseDTO> obtenerPagosPendientes() {
        return mapper.toResponseDTOList(
                pagoRepository.findPagosPendientesValidacion()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponseDTO> obtenerPagosPorEstado(EstadoPago estado) {
        return mapper.toResponseDTOList(
                pagoRepository.findByEstado(estado)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PagoResponseDTO obtenerPagoPorPedidoOnline(Long pedidoId) {
        PagoEntity pago = pagoRepository.findByPedidoOnlineId(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado para este pedido"));
        return mapper.toResponseDTO(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public PagoResponseDTO obtenerPagoPorVentaFisica(Long ventaId) {
        PagoEntity pago = pagoRepository.findByVentaFisicaId(ventaId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado para esta venta"));
        return mapper.toResponseDTO(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponseDTO> obtenerPagosPorRangoFechas(Date fechaInicio, Date fechaFin) {
        return mapper.toResponseDTOList(
                pagoRepository.findByFechaBetween(fechaInicio, fechaFin)
        );
    }

    // ========= SUBIR COMPROBANTE (CLIENTE) =========

    @Override
    @Transactional
    public PagoResponseDTO subirComprobante(Long pedidoId,
                                            MultipartFile archivo,
                                            String numeroOperacion,
                                            String username) {

        if (archivo == null || archivo.isEmpty()) {
            throw new RuntimeException("El archivo de comprobante está vacío.");
        }

        // Solo imágenes
        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("El comprobante debe ser una imagen (JPG/PNG).");
        }

        PedidoOnlineEntity pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!pedido.getUsuario().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para subir comprobante de este pedido.");
        }

        if (pedido.getEstado() != EstadoPedido.PENDIENTE_PAGO) {
            throw new RuntimeException("Solo puedes subir comprobante de pedidos pendientes de pago.");
        }

        // Obtener o crear pago
        PagoEntity pago = pagoRepository.findByPedidoOnlineId(pedidoId)
                .orElseGet(() -> {
                    PagoEntity nuevo = new PagoEntity();
                    nuevo.setPedidoOnline(pedido);
                    nuevo.setMetodoPago(MetodoPago.YAPE);
                    nuevo.setMonto(pedido.getTotal());
                    nuevo.setEstado(EstadoPago.PENDIENTE);
                    return nuevo;
                });

        // Guardar archivo
        String urlComprobante = guardarArchivoComprobante(pedidoId, archivo);

        pago.setNumeroOperacion(numeroOperacion);
        pago.setFechaPago(new Date());
        pago.setComprobanteUrl(urlComprobante);
        pago.setEstado(EstadoPago.PENDIENTE);

        PagoEntity guardado = pagoRepository.save(pago);
        pedido.setPago(guardado);          // asegurar relación
        pedidoRepository.save(pedido);

        return mapper.toResponseDTO(guardado);
    }

    /**
     * Guarda el archivo en "static.imagenes/pagos"
     * y devuelve una URL pública tipo: /imagenes/pagos/pedido_15_2025-11-25_1830.png
     */
    private String guardarArchivoComprobante(Long pedidoId, MultipartFile archivo) {
        try {
            Path uploadDir = Paths.get("static.imagenes/pagos").toAbsolutePath().normalize();
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String originalFilename = archivo.getOriginalFilename();
            String extension = ".jpg";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
            String filename = "pedido_" + pedidoId + "_" + timestamp + extension;

            Path destinationFile = uploadDir.resolve(filename).normalize();
            Files.copy(archivo.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            return "/imagenes/pagos/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el comprobante de pago: " + e.getMessage(), e);
        }
    }

    // ========= VALIDAR / RECHAZAR PAGO =========

    @Override
    @Transactional
    public PagoResponseDTO validarPago(Long id, ValidarPagoDTO dto, String username) {
        PagoEntity pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        UsuarioEntity validador = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (pago.getEstado() != EstadoPago.PENDIENTE) {
            throw new RuntimeException("El pago ya fue validado o rechazado.");
        }

        if (pago.getComprobanteUrl() == null || pago.getComprobanteUrl().isBlank()) {
            throw new RuntimeException("No se ha subido comprobante para este pago.");
        }

        pago.validar(validador);
        if (dto.getObservaciones() != null) {
            pago.setObservaciones(dto.getObservaciones());
        }

        if (pago.getPedidoOnline() != null) {
            PedidoOnlineEntity pedido = pago.getPedidoOnline();

            if (pedido.getEstado() != EstadoPedido.PAGADO) {
                pedido.setEstado(EstadoPedido.PAGADO);
                pedido.setModificadoPor(username);
                descontarStockPedido(pedido, validador);
                pedidoRepository.save(pedido);
            }
        }

        PagoEntity pagoActualizado = pagoRepository.save(pago);
        return mapper.toResponseDTO(pagoActualizado);
    }

    @Override
    @Transactional
    public PagoResponseDTO rechazarPago(Long id, RechazarPagoDTO dto, String username) {
        PagoEntity pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        UsuarioEntity validador = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (pago.getEstado() != EstadoPago.PENDIENTE) {
            throw new RuntimeException("El pago ya fue validado o rechazado.");
        }

        pago.rechazar(validador, dto.getMotivoRechazo());

        PagoEntity pagoActualizado = pagoRepository.save(pago);
        return mapper.toResponseDTO(pagoActualizado);
    }

    // ========= DESCONTAR STOCK =========

    private void descontarStockPedido(PedidoOnlineEntity pedido, UsuarioEntity usuarioValidador) {
        for (PedidoItemEntity item : pedido.getItems()) {
            ProductEntity producto = item.getProducto();
            int stockAnterior = producto.getStock();

            producto.descontarStock(item.getCantidad());
            productRepository.save(producto);

            MovimientoStockEntity movimiento = new MovimientoStockEntity();
            movimiento.setProducto(producto);
            movimiento.setTipoMovimiento(TipoMovimiento.VENTA_ONLINE);
            movimiento.setCantidad(-item.getCantidad());
            movimiento.setStockAnterior(stockAnterior);
            movimiento.setStockNuevo(producto.getStock());
            movimiento.setMotivo("Venta online - Pedido #" + pedido.getId());
            movimiento.setUsuario(usuarioValidador);
            movimiento.setPedidoOnline(pedido);

            movimientoStockRepository.save(movimiento);
        }
    }
}
