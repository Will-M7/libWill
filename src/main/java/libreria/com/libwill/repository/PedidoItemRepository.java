package libreria.com.libwill.repository;

import libreria.com.libwill.entity.PedidoItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PedidoItemRepository extends JpaRepository<PedidoItemEntity, Long> {

    // Buscar items por pedido
    List<PedidoItemEntity> findByPedidoId(Long pedidoId);

    // Buscar items por producto
    List<PedidoItemEntity> findByProductoId(Long productoId);

    // Productos más vendidos en pedidos online
    @Query("SELECT pi.producto.id, pi.producto.name, SUM(pi.cantidad) as total " +
            "FROM PedidoItemEntity pi " +
            "WHERE pi.pedido.estado IN ('PAGADO', 'ENVIADO', 'RECIBIDO') " +
            "AND pi.pedido.fechaPedido BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY pi.producto.id, pi.producto.name " +
            "ORDER BY total DESC")
    List<Object[]> findProductosMasVendidosOnline(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    // Total de unidades vendidas por producto
    @Query("SELECT SUM(pi.cantidad) FROM PedidoItemEntity pi WHERE pi.producto.id = :productoId AND pi.pedido.estado IN ('PAGADO', 'ENVIADO', 'RECIBIDO')")
    Long totalUnidadesVendidasPorProducto(@Param("productoId") Long productoId);
}