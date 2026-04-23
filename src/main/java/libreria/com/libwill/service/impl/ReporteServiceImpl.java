package libreria.com.libwill.service.impl;

import libreria.com.libwill.dto.response.ReporteVentasDTO;
import libreria.com.libwill.entity.ProductEntity;
import libreria.com.libwill.entity.enums.EstadoPedido;
import libreria.com.libwill.repository.*;
import libreria.com.libwill.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private PedidoOnlineRepository pedidoRepository;

    @Autowired
    private VentaFisicaRepository ventaRepository;

    @Autowired
    private PedidoItemRepository pedidoItemRepository;

    @Autowired
    private VentaItemRepository ventaItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Override
    @Transactional(readOnly = true)
    public ReporteVentasDTO obtenerReporteGeneral(Date fechaInicio, Date fechaFin) {
        ReporteVentasDTO reporte = new ReporteVentasDTO();

        // Totales online
        Double totalOnline = pedidoRepository.calcularTotalVentasOnline(fechaInicio, fechaFin);
        reporte.setMontoTotalOnline(totalOnline != null ? BigDecimal.valueOf(totalOnline) : BigDecimal.ZERO);

        // Totales física
        Double totalFisica = ventaRepository.calcularTotalVentasFisicas(fechaInicio, fechaFin);
        reporte.setMontoTotalFisica(totalFisica != null ? BigDecimal.valueOf(totalFisica) : BigDecimal.ZERO);

        // Total general
        reporte.setMontoTotalGeneral(reporte.getMontoTotalOnline().add(reporte.getMontoTotalFisica()));

        // Contadores
        reporte.setTotalPedidosOnline(pedidoRepository.count());
        reporte.setTotalVentasFisicas(ventaRepository.count());
        reporte.setPedidosPendientes(pedidoRepository.countByEstado(EstadoPedido.PENDIENTE_PAGO));
        reporte.setPedidosPagados(pedidoRepository.countByEstado(EstadoPedido.PAGADO));
        reporte.setPedidosEnviados(pedidoRepository.countByEstado(EstadoPedido.ENVIADO));
        reporte.setPedidosRecibidos(pedidoRepository.countByEstado(EstadoPedido.RECIBIDO));

        reporte.setTotalVentas(reporte.getTotalPedidosOnline() + reporte.getTotalVentasFisicas());

        return reporte;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerProductosMasVendidos(Date fechaInicio, Date fechaFin) {
        List<Object[]> resultadosOnline = pedidoItemRepository.findProductosMasVendidosOnline(fechaInicio, fechaFin);
        List<Object[]> resultadosFisica = ventaItemRepository.findProductosMasVendidosFisica(fechaInicio, fechaFin);

        // Combinar resultados
        Map<Long, Map<String, Object>> productoMap = new HashMap<>();

        for (Object[] row : resultadosOnline) {
            Long productoId = (Long) row[0];
            String nombre = (String) row[1];
            Long cantidad = (Long) row[2];

            Map<String, Object> producto = new HashMap<>();
            producto.put("productoId", productoId);
            producto.put("nombre", nombre);
            producto.put("cantidadOnline", cantidad);
            producto.put("cantidadFisica", 0L);
            producto.put("cantidadTotal", cantidad);

            productoMap.put(productoId, producto);
        }

        for (Object[] row : resultadosFisica) {
            Long productoId = (Long) row[0];
            String nombre = (String) row[1];
            Long cantidad = (Long) row[2];

            if (productoMap.containsKey(productoId)) {
                Map<String, Object> producto = productoMap.get(productoId);
                producto.put("cantidadFisica", cantidad);
                producto.put("cantidadTotal", (Long) producto.get("cantidadOnline") + cantidad);
            } else {
                Map<String, Object> producto = new HashMap<>();
                producto.put("productoId", productoId);
                producto.put("nombre", nombre);
                producto.put("cantidadOnline", 0L);
                producto.put("cantidadFisica", cantidad);
                producto.put("cantidadTotal", cantidad);
                productoMap.put(productoId, producto);
            }
        }

        return productoMap.values().stream()
                .sorted((p1, p2) -> Long.compare((Long) p2.get("cantidadTotal"), (Long) p1.get("cantidadTotal")))
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerRankingVendedores(Date fechaInicio, Date fechaFin) {
        List<Object[]> ranking = ventaRepository.rankingVendedoresPorMonto(fechaInicio, fechaFin);

        return ranking.stream().map(row -> {
            Map<String, Object> vendedor = new HashMap<>();
            vendedor.put("vendedorId", row[0]);
            vendedor.put("nombre", row[1]);
            vendedor.put("apellido", row[2]);
            vendedor.put("totalVentas", row[3]);
            return vendedor;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> obtenerVentasPorMetodoPago(Date fechaInicio, Date fechaFin) {
        Map<String, Object> resultado = new HashMap<>();

        long totalYape = pagoRepository.countByMetodoPago(libreria.com.libwill.entity.enums.MetodoPago.YAPE);
        long totalEfectivo = pagoRepository.countByMetodoPago(libreria.com.libwill.entity.enums.MetodoPago.EFECTIVO);

        resultado.put("totalYape", totalYape);
        resultado.put("totalEfectivo", totalEfectivo);
        resultado.put("totalGeneral", totalYape + totalEfectivo);

        return resultado;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerResumenInventario() {
        List<ProductEntity> productos = productRepository.findAll();

        return productos.stream().map(p -> {
            Map<String, Object> item = new HashMap<>();
            item.put("productoId", p.getId());
            item.put("nombre", p.getName());
            item.put("sku", p.getSku());
            item.put("stock", p.getStock());
            item.put("precioMinor", p.getPriceMinor());
            item.put("priceMajor", p.getPriceMajor());
            item.put("activo", p.getActive());
            item.put("estado", p.getStock() == 0 ? "AGOTADO" : p.getStock() < 10 ? "BAJO" : "DISPONIBLE");
            return item;
        }).collect(Collectors.toList());
    }}