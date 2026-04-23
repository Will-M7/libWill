package libreria.com.libwill.repository;

import libreria.com.libwill.entity.BoletaEntity;
import libreria.com.libwill.entity.enums.TipoComprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoletaRepository extends JpaRepository<BoletaEntity, Long> {

    // Buscar boleta por pedido online
    Optional<BoletaEntity> findByPedidoOnlineId(Long pedidoOnlineId);

    // Buscar boleta por venta física
    Optional<BoletaEntity> findByVentaFisicaId(Long ventaFisicaId);

    // Buscar boletas por tipo
    List<BoletaEntity> findByTipoComprobante(TipoComprobante tipoComprobante);

    // Buscar por número completo
    Optional<BoletaEntity> findByNumeroCompleto(String numeroCompleto);

    // Buscar boletas por serie
    List<BoletaEntity> findBySerie(String serie);

    // Obtener el último número de boleta por serie
    @Query("SELECT b FROM BoletaEntity b WHERE b.serie = :serie ORDER BY b.numero DESC LIMIT 1")
    Optional<BoletaEntity> findUltimaBoletaPorSerie(@Param("serie") String serie);

    // Buscar boletas en un rango de fechas
    @Query("SELECT b FROM BoletaEntity b WHERE b.fechaEmision BETWEEN :fechaInicio AND :fechaFin ORDER BY b.fechaEmision DESC")
    List<BoletaEntity> findByFechaEmisionBetween(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    // Total de boletas emitidas
    long countByTipoComprobante(TipoComprobante tipoComprobante);

    // Buscar boletas por documento del cliente
    List<BoletaEntity> findByNumeroDocumento(String numeroDocumento);

    // Total facturado en un rango de fechas
    @Query("SELECT SUM(b.total) FROM BoletaEntity b WHERE b.fechaEmision BETWEEN :fechaInicio AND :fechaFin")
    Double calcularTotalFacturado(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);
}