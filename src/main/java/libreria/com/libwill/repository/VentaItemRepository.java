package libreria.com.libwill.repository;

import libreria.com.libwill.entity.VentaItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VentaItemRepository extends JpaRepository<VentaItemEntity, Long> {

    // Buscar items por venta
    List<VentaItemEntity> findByVentaId(Long ventaId);

    // Buscar items por producto
    List<VentaItemEntity> findByProductoId(Long productoId);

    // Productos más vendidos en tienda física
    @Query("SELECT vi.producto.id, vi.producto.name, SUM(vi.cantidad) as total " +
            "FROM VentaItemEntity vi " +
            "WHERE vi.venta.fechaVenta BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY vi.producto.id, vi.producto.name " +
            "ORDER BY total DESC")
    List<Object[]> findProductosMasVendidosFisica(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    // Total de unidades vendidas por producto en tienda física
    @Query("SELECT SUM(vi.cantidad) FROM VentaItemEntity vi WHERE vi.producto.id = :productoId")
    Long totalUnidadesVendidasFisicaPorProducto(@Param("productoId") Long productoId);

    // Items vendidos por un vendedor específico
    @Query("SELECT vi FROM VentaItemEntity vi WHERE vi.venta.vendedor.id = :vendedorId AND vi.venta.fechaVenta BETWEEN :fechaInicio AND :fechaFin")
    List<VentaItemEntity> findItemsPorVendedorYFecha(
            @Param("vendedorId") Long vendedorId,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin
    );
}