package libreria.com.libwill.repository;

import libreria.com.libwill.entity.MovimientoStockEntity;
import libreria.com.libwill.entity.enums.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MovimientoStockRepository extends JpaRepository<MovimientoStockEntity, Long> {

    // Buscar movimientos por producto
    List<MovimientoStockEntity> findByProductoIdOrderByFechaMovimientoDesc(Long productoId);

    // Buscar movimientos por tipo
    List<MovimientoStockEntity> findByTipoMovimiento(TipoMovimiento tipoMovimiento);

    // Buscar movimientos por usuario
    List<MovimientoStockEntity> findByUsuarioIdOrderByFechaMovimientoDesc(Long usuarioId);

    // Buscar movimientos en un rango de fechas
    @Query("SELECT m FROM MovimientoStockEntity m WHERE m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fechaMovimiento DESC")
    List<MovimientoStockEntity> findByFechaBetween(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    // Buscar movimientos por producto y tipo
    @Query("SELECT m FROM MovimientoStockEntity m WHERE m.producto.id = :productoId AND m.tipoMovimiento = :tipo ORDER BY m.fechaMovimiento DESC")
    List<MovimientoStockEntity> findByProductoAndTipo(@Param("productoId") Long productoId, @Param("tipo") TipoMovimiento tipo);

    // Total de ingresos de stock por producto
    @Query("SELECT SUM(m.cantidad) FROM MovimientoStockEntity m WHERE m.producto.id = :productoId AND m.tipoMovimiento = 'INGRESO'")
    Long totalIngresosStock(@Param("productoId") Long productoId);

    // Total de salidas de stock por producto
    @Query("SELECT SUM(ABS(m.cantidad)) FROM MovimientoStockEntity m WHERE m.producto.id = :productoId AND m.tipoMovimiento IN ('VENTA_ONLINE', 'VENTA_FISICA')")
    Long totalSalidasStock(@Param("productoId") Long productoId);

    // Últimos movimientos (para auditoría)
    @Query("SELECT m FROM MovimientoStockEntity m ORDER BY m.fechaMovimiento DESC LIMIT 50")
    List<MovimientoStockEntity> findUltimosMovimientos();
}