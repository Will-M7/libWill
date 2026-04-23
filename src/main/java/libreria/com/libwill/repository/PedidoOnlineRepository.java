package libreria.com.libwill.repository;

import libreria.com.libwill.entity.PedidoOnlineEntity;
import libreria.com.libwill.entity.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PedidoOnlineRepository extends JpaRepository<PedidoOnlineEntity, Long> {

    // Buscar pedidos por usuario
    List<PedidoOnlineEntity> findByUsuarioId(Long usuarioId);

    // Buscar pedidos por usuario y estado
    List<PedidoOnlineEntity> findByUsuarioIdAndEstado(Long usuarioId, EstadoPedido estado);

    // Buscar pedidos por estado (para admin)
    List<PedidoOnlineEntity> findByEstado(EstadoPedido estado);

    // Buscar pedidos por username del usuario
    @Query("SELECT p FROM PedidoOnlineEntity p WHERE p.usuario.username = :username ORDER BY p.fechaPedido DESC")
    List<PedidoOnlineEntity> findByUsername(@Param("username") String username);

    // Contar pedidos por usuario
    long countByUsuarioId(Long usuarioId);

    // Buscar pedidos por rango de fechas
    @Query("SELECT p FROM PedidoOnlineEntity p WHERE p.fechaPedido BETWEEN :fechaInicio AND :fechaFin ORDER BY p.fechaPedido DESC")
    List<PedidoOnlineEntity> findByFechaBetween(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    // Buscar pedidos pendientes de pago
    @Query("SELECT p FROM PedidoOnlineEntity p WHERE p.estado = 'PENDIENTE_PAGO' ORDER BY p.fechaPedido DESC")
    List<PedidoOnlineEntity> findPendientesPago();

    // Buscar pedidos pagados pero no enviados
    @Query("SELECT p FROM PedidoOnlineEntity p WHERE p.estado = 'PAGADO' ORDER BY p.fechaPedido DESC")
    List<PedidoOnlineEntity> findPagadosNoEnviados();

    // Total de ventas online en un rango de fechas
    @Query("SELECT SUM(p.total) FROM PedidoOnlineEntity p WHERE p.estado IN ('PAGADO', 'ENVIADO', 'RECIBIDO') AND p.fechaPedido BETWEEN :fechaInicio AND :fechaFin")
    Double calcularTotalVentasOnline(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    // Contar pedidos por estado
    long countByEstado(EstadoPedido estado);
}