package libreria.com.libwill.service.impl;

import libreria.com.libwill.dto.response.BoletaResponseDTO;
import libreria.com.libwill.entity.BoletaEntity;
import libreria.com.libwill.entity.enums.TipoComprobante;
import libreria.com.libwill.mapper.BoletaMapper;
import libreria.com.libwill.repository.BoletaRepository;
import libreria.com.libwill.service.BoletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BoletaServiceImpl implements BoletaService {

    @Autowired
    private BoletaRepository boletaRepository;

    @Autowired
    private BoletaMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public BoletaResponseDTO obtenerBoletaPorId(Long id) {
        BoletaEntity boleta = boletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Boleta no encontrada"));
        return mapper.toResponseDTO(boleta);
    }

    @Override
    @Transactional(readOnly = true)
    public BoletaResponseDTO obtenerBoletaPorPedido(Long pedidoId) {
        BoletaEntity boleta = boletaRepository.findByPedidoOnlineId(pedidoId)
                .orElseThrow(() -> new RuntimeException("Boleta no encontrada para este pedido"));
        return mapper.toResponseDTO(boleta);
    }

    @Override
    @Transactional(readOnly = true)
    public BoletaResponseDTO obtenerBoletaPorVenta(Long ventaId) {
        BoletaEntity boleta = boletaRepository.findByVentaFisicaId(ventaId)
                .orElseThrow(() -> new RuntimeException("Boleta no encontrada para esta venta"));
        return mapper.toResponseDTO(boleta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoletaResponseDTO> obtenerTodasLasBoletas() {
        List<BoletaEntity> boletas = boletaRepository.findAll();
        return mapper.toResponseDTOList(boletas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoletaResponseDTO> obtenerBoletasPorTipo(TipoComprobante tipo) {
        List<BoletaEntity> boletas = boletaRepository.findByTipoComprobante(tipo);
        return mapper.toResponseDTOList(boletas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoletaResponseDTO> obtenerBoletasPorFecha(Date fechaInicio, Date fechaFin) {
        List<BoletaEntity> boletas = boletaRepository.findByFechaEmisionBetween(fechaInicio, fechaFin);
        return mapper.toResponseDTOList(boletas);
    }

    @Override
    public byte[] generarBoletaPDF(Long boletaId) {
        // TODO: Implementar generación de PDF con iText o similar
        throw new UnsupportedOperationException("Generación de PDF pendiente de implementación");
    }
}