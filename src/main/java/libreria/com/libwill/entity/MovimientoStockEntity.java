package libreria.com.libwill.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import libreria.com.libwill.entity.enums.TipoMovimiento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "movimientos_stock")
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoStockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductEntity producto;

    @NotNull(message = "El tipo de movimiento no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false, length = 20)
    private TipoMovimiento tipoMovimiento;

    @NotNull(message = "La cantidad no puede ser nula")
    @Column(nullable = false)
    private Integer cantidad; // Positivo para ingreso, negativo para salida

    @NotNull(message = "El stock anterior no puede ser nulo")
    @Column(name = "stock_anterior", nullable = false)
    private Integer stockAnterior;

    @NotNull(message = "El stock nuevo no puede ser nulo")
    @Column(name = "stock_nuevo", nullable = false)
    private Integer stockNuevo;

    @Column(length = 255)
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_online_id")
    private PedidoOnlineEntity pedidoOnline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_fisica_id")
    private VentaFisicaEntity ventaFisica;

    @NotNull(message = "La fecha de movimiento no puede ser nula")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_movimiento", nullable = false)
    private Date fechaMovimiento;

    @PrePersist
    protected void onCreate() {
        this.fechaMovimiento = new Date();
    }
}