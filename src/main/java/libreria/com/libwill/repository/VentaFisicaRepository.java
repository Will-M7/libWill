package libreria.com.libwill.repository;

import libreria.com.libwill.entity.VentaFisicaEntity;
import libreria.com.libwill.entity.enums.TipoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VentaFisicaRepository extends JpaRepository<VentaFisicaEntity, Long> {

    // Buscar ventas por vendedor
    List<VentaFisicaEntity> findByVendedorId(Long vendedorId);

    // Buscar ventas por tipo
    List<VentaFisicaEntity> findByTipoVenta(TipoVenta tipoVenta);

    // Buscar ventas por vendedor y rango de fechas
    @Query("SELECT v FROM VentaFisicaEntity v WHERE v.vendedor.id = :vendedorId AND v.fechaVenta BETWEEN :fechaInicio AND :fechaFin ORDER BY v.fechaVenta DESC")
    List<VentaFisicaEntity> findByVendedorAndFechaBetween(
            @Param("vendedorId") Long vendedorId,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin
    );

    // Buscar todas las ventas en un rango de fechas
    @Query("SELECT v FROM VentaFisicaEntity v WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin ORDER BY v.fechaVenta DESC")
    List<VentaFisicaEntity> findByFechaBetween(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    // Total de ventas físicas por vendedor
    @Query("SELECT SUM(v.total) FROM VentaFisicaEntity v WHERE v.vendedor.id = :vendedorId AND v.fechaVenta BETWEEN :fechaInicio AND :fechaFin")
    Double calcularTotalVentasPorVendedor(
            @Param("vendedorId") Long vendedorId,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin
    );

    // Total de ventas físicas en un rango de fechas
    @Query("SELECT SUM(v.total) FROM VentaFisicaEntity v WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin")
    Double calcularTotalVentasFisicas(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    // Contar ventas por vendedor
    long countByVendedorId(Long vendedorId);

    // Ventas del día por vendedor
    @Query("SELECT v FROM VentaFisicaEntity v WHERE v.vendedor.id = :vendedorId AND DATE(v.fechaVenta) = DATE(:fecha)")
    List<VentaFisicaEntity> findVentasDelDiaPorVendedor(@Param("vendedorId") Long vendedorId, @Param("fecha") Date fecha);

    // Ranking de vendedores por monto vendido
    @Query("SELECT v.vendedor.id, v.vendedor.nombre, v.vendedor.apellido, SUM(v.total) as totalVentas " +
            "FROM VentaFisicaEntity v " +
            "WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY v.vendedor.id, v.vendedor.nombre, v.vendedor.apellido " +
            "ORDER BY totalVentas DESC")
    List<Object[]> rankingVendedoresPorMonto(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);
}