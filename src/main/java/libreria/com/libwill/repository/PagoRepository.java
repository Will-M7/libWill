package libreria.com.libwill.repository;

import libreria.com.libwill.entity.PagoEntity;
import libreria.com.libwill.entity.enums.EstadoPago;
import libreria.com.libwill.entity.enums.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<PagoEntity, Long> {

    Optional<PagoEntity> findByPedidoOnlineId(Long pedidoOnlineId);

    Optional<PagoEntity> findByVentaFisicaId(Long ventaFisicaId);

    List<PagoEntity> findByEstado(EstadoPago estado);

    @Query("SELECT p FROM PagoEntity p WHERE p.estado = 'PENDIENTE' ORDER BY p.fechaPago ASC")
    List<PagoEntity> findPagosPendientesValidacion();

    List<PagoEntity> findByMetodoPago(MetodoPago metodoPago);

    @Query("SELECT p FROM PagoEntity p WHERE p.fechaPago BETWEEN :fechaInicio AND :fechaFin ORDER BY p.fechaPago DESC")
    List<PagoEntity> findByFechaBetween(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    @Query("SELECT SUM(p.monto) FROM PagoEntity p WHERE p.estado = 'VALIDADO' AND p.fechaPago BETWEEN :fechaInicio AND :fechaFin")
    Double calcularTotalPagosValidados(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    @Query("SELECT p FROM PagoEntity p WHERE p.validadoPor.id = :usuarioId ORDER BY p.fechaValidacion DESC")
    List<PagoEntity> findPagosValidadosPorUsuario(@Param("usuarioId") Long usuarioId);

    long countByEstado(EstadoPago estado);

    long countByMetodoPago(MetodoPago metodoPago);
}
