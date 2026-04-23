package libreria.com.libwill.service.impl;

import libreria.com.libwill.dto.request.AgregarStockDTO;
import libreria.com.libwill.dto.response.MovimientoStockResponseDTO;
import libreria.com.libwill.entity.MovimientoStockEntity;
import libreria.com.libwill.entity.ProductEntity;
import libreria.com.libwill.entity.UsuarioEntity;
import libreria.com.libwill.entity.enums.TipoMovimiento;
import libreria.com.libwill.mapper.MovimientoStockMapper;
import libreria.com.libwill.repository.MovimientoStockRepository;
import libreria.com.libwill.repository.ProductRepository;
import libreria.com.libwill.repository.UsuarioRepository;
import libreria.com.libwill.service.MovimientoStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class MovimientoStockServiceImpl implements MovimientoStockService {

    @Autowired
    private MovimientoStockRepository movimientoRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MovimientoStockMapper mapper;

    @Override
    @Transactional
    public MovimientoStockResponseDTO agregarStock(AgregarStockDTO dto, String username) {
        ProductEntity producto = productRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        UsuarioEntity usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        int stockAnterior = producto.getStock();
        producto.agregarStock(dto.getCantidad());
        productRepository.save(producto);

        MovimientoStockEntity movimiento = new MovimientoStockEntity();
        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento(TipoMovimiento.INGRESO);
        movimiento.setCantidad(dto.getCantidad());
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockNuevo(producto.getStock());
        movimiento.setMotivo(dto.getMotivo());
        movimiento.setUsuario(usuario);

        MovimientoStockEntity movimientoGuardado = movimientoRepository.save(movimiento);
        return mapper.toResponseDTO(movimientoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoStockResponseDTO> obtenerMovimientosPorProducto(Long productoId) {
        List<MovimientoStockEntity> movimientos = movimientoRepository.findByProductoIdOrderByFechaMovimientoDesc(productoId);
        return mapper.toResponseDTOList(movimientos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoStockResponseDTO> obtenerMovimientosPorTipo(TipoMovimiento tipo) {
        List<MovimientoStockEntity> movimientos = movimientoRepository.findByTipoMovimiento(tipo);
        return mapper.toResponseDTOList(movimientos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoStockResponseDTO> obtenerMovimientosPorFecha(Date fechaInicio, Date fechaFin) {
        List<MovimientoStockEntity> movimientos = movimientoRepository.findByFechaBetween(fechaInicio, fechaFin);
        return mapper.toResponseDTOList(movimientos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoStockResponseDTO> obtenerUltimosMovimientos() {
        List<MovimientoStockEntity> movimientos = movimientoRepository.findUltimosMovimientos();
        return mapper.toResponseDTOList(movimientos);
    }
}