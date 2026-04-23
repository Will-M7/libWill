package libreria.com.libwill.service.impl;

import libreria.com.libwill.dto.request.CreateBoletaDTO;
import libreria.com.libwill.dto.request.CreateVentaFisicaDTO;
import libreria.com.libwill.dto.request.CreateVentaItemDTO;
import libreria.com.libwill.dto.response.VentaFisicaResponseDTO;
import libreria.com.libwill.entity.*;
import libreria.com.libwill.entity.enums.*;
import libreria.com.libwill.mapper.VentaFisicaMapper;
import libreria.com.libwill.repository.*;
import libreria.com.libwill.service.VentaFisicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class VentaFisicaServiceImpl implements VentaFisicaService {

    @Autowired
    private VentaFisicaRepository ventaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private BoletaRepository boletaRepository;

    @Autowired
    private MovimientoStockRepository movimientoStockRepository;

    @Autowired
    private VentaFisicaMapper mapper;

    private static final int CANTIDAD_MAYORISTA = 12;

    @Override
    @Transactional
    public VentaFisicaResponseDTO crearVenta(CreateVentaFisicaDTO dto, String username) {
        // 1. Buscar vendedor
        UsuarioEntity vendedor = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));

        // 2. Validar stock
        for (CreateVentaItemDTO itemDTO : dto.getItems()) {
            ProductEntity producto = productRepository.findById(itemDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + itemDTO.getProductoId()));

            if (!producto.tieneStock(itemDTO.getCantidad())) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getName() +
                        ". Disponible: " + producto.getStock() + ", Solicitado: " + itemDTO.getCantidad());
            }
        }

        // 3. Crear venta
        VentaFisicaEntity venta = new VentaFisicaEntity();
        venta.setVendedor(vendedor);
        venta.setTipoVenta(dto.getTipoVenta());
        venta.setObservaciones(dto.getObservaciones());

        // 4. Agregar items
        BigDecimal total = BigDecimal.ZERO;
        for (CreateVentaItemDTO itemDTO : dto.getItems()) {
            ProductEntity producto = productRepository.findById(itemDTO.getProductoId()).get();

            VentaItemEntity item = new VentaItemEntity();
            item.setProducto(producto);
            item.setCantidad(itemDTO.getCantidad());

            // Determinar precio
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

            venta.addItem(item);
        }

        venta.setTotal(total);

        // 5. Crear pago (siempre validado en venta física)
        PagoEntity pago = new PagoEntity();
        pago.setVentaFisica(venta);
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setMonto(total);
        pago.setNumeroOperacion(dto.getNumeroOperacion());
        pago.setEstado(EstadoPago.VALIDADO);
        pago.setFechaValidacion(new Date());
        pago.setValidadoPor(vendedor);
        venta.setPago(pago);

        // 6. Guardar venta
        VentaFisicaEntity ventaGuardada = ventaRepository.save(venta);

        // 7. Descontar stock
        descontarStockVenta(ventaGuardada, username);

        // 8. Generar boleta si es CON_BOLETA
        if (dto.getTipoVenta() == TipoVenta.CON_BOLETA && dto.getDatosCliente() != null) {
            generarBoleta(ventaGuardada, dto.getDatosCliente());
        }

        return mapper.toResponseDTO(ventaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaFisicaResponseDTO> obtenerMisVentas(String username) {
        UsuarioEntity vendedor = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));

        List<VentaFisicaEntity> ventas = ventaRepository.findByVendedorId(vendedor.getId());
        return mapper.toResponseDTOList(ventas);
    }

    @Override
    @Transactional(readOnly = true)
    public VentaFisicaResponseDTO obtenerVentaPorId(Long id, String username) {
        VentaFisicaEntity venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        // Verificar permisos
        UsuarioEntity usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isAdmin = usuario.getRoles().stream()
                .anyMatch(rol -> rol.getNombre().equals("ROLE_ADMIN"));

        if (!isAdmin && !venta.getVendedor().getUsername().equals(username)) {
            throw new AccessDeniedException("No tienes permiso para ver esta venta");
        }

        return mapper.toResponseDTO(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaFisicaResponseDTO> obtenerTodasLasVentas() {
        List<VentaFisicaEntity> ventas = ventaRepository.findAll();
        return mapper.toResponseDTOList(ventas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaFisicaResponseDTO> obtenerVentasPorVendedor(Long vendedorId) {
        List<VentaFisicaEntity> ventas = ventaRepository.findByVendedorId(vendedorId);
        return mapper.toResponseDTOList(ventas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaFisicaResponseDTO> obtenerVentasPorFecha(Date fechaInicio, Date fechaFin) {
        List<VentaFisicaEntity> ventas = ventaRepository.findByFechaBetween(fechaInicio, fechaFin);
        return mapper.toResponseDTOList(ventas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaFisicaResponseDTO> obtenerVentasDelDia(String username) {
        UsuarioEntity vendedor = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));

        Date hoy = new Date();
        List<VentaFisicaEntity> ventas = ventaRepository.findVentasDelDiaPorVendedor(vendedor.getId(), hoy);
        return mapper.toResponseDTOList(ventas);
    }

    // Método auxiliar: Descontar stock
    private void descontarStockVenta(VentaFisicaEntity venta, String username) {
        UsuarioEntity usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        for (VentaItemEntity item : venta.getItems()) {
            ProductEntity producto = item.getProducto();
            int stockAnterior = producto.getStock();

            producto.descontarStock(item.getCantidad());
            productRepository.save(producto);

            // Registrar movimiento
            MovimientoStockEntity movimiento = new MovimientoStockEntity();
            movimiento.setProducto(producto);
            movimiento.setTipoMovimiento(TipoMovimiento.VENTA_FISICA);
            movimiento.setCantidad(-item.getCantidad());
            movimiento.setStockAnterior(stockAnterior);
            movimiento.setStockNuevo(producto.getStock());
            movimiento.setMotivo("Venta física - Venta #" + venta.getId());
            movimiento.setUsuario(usuario);
            movimiento.setVentaFisica(venta);
            movimientoStockRepository.save(movimiento);
        }
    }

    // Método auxiliar: Generar boleta
    private void generarBoleta(VentaFisicaEntity venta, CreateBoletaDTO datosCliente) {
        String serie = datosCliente.getTipoComprobante() == TipoComprobante.BOLETA ? "B001" : "F001";

        // Obtener último número
        String siguienteNumero = obtenerSiguienteNumero(serie);

        // Calcular montos
        BigDecimal subtotal = venta.getTotal().divide(new BigDecimal("1.18"), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal igv = venta.getTotal().subtract(subtotal);

        BoletaEntity boleta = new BoletaEntity();
        boleta.setTipoComprobante(datosCliente.getTipoComprobante());
        boleta.setSerie(serie);
        boleta.setNumero(siguienteNumero);
        boleta.setNumeroCompleto(serie + "-" + siguienteNumero);
        boleta.setTipoDocumentoCliente(datosCliente.getTipoDocumento());
        boleta.setNumeroDocumento(datosCliente.getNumeroDocumento());
        boleta.setNombreCliente(datosCliente.getNombreCliente());
        boleta.setDireccionCliente(datosCliente.getDireccionCliente());
        boleta.setSubtotal(subtotal);
        boleta.setIgv(igv);
        boleta.setTotal(venta.getTotal());
        boleta.setVentaFisica(venta);

        boletaRepository.save(boleta);
    }

    // Método auxiliar: Obtener siguiente número de boleta
    private String obtenerSiguienteNumero(String serie) {
        return boletaRepository.findUltimaBoletaPorSerie(serie)
                .map(b -> {
                    int numero = Integer.parseInt(b.getNumero()) + 1;
                    return String.format("%08d", numero);
                })
                .orElse("00000001");
    }
}