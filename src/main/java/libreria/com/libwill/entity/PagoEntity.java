package libreria.com.libwill.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import libreria.com.libwill.entity.enums.EstadoPago;
import libreria.com.libwill.entity.enums.MetodoPago;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "pagos")
@NoArgsConstructor
@AllArgsConstructor
public class PagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Relación con PedidoOnline (puede ser null)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_online_id")
    private PedidoOnlineEntity pedidoOnline;

    // 🔗 Relación con VentaFisica (puede ser null)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_fisica_id")
    private VentaFisicaEntity ventaFisica;

    @NotNull(message = "El método de pago no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false, length = 20)
    private MetodoPago metodoPago;

    @NotNull(message = "El monto no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "numero_operacion", length = 100)
    private String numeroOperacion; // Para Yape

    @NotNull(message = "El estado del pago no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPago estado = EstadoPago.PENDIENTE;

    @NotNull(message = "La fecha de pago no puede ser nula")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_pago", nullable = false)
    private Date fechaPago;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_validacion")
    private Date fechaValidacion;

    // 🔗 Usuario que validó el pago (admin/vendedor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validado_por")
    private UsuarioEntity validadoPor;

    @Column(name = "comprobante_url", length = 255)
    private String comprobanteUrl; // Screenshot del Yape

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "motivo_rechazo", columnDefinition = "TEXT")
    private String motivoRechazo;

    // 📅 Auditoría
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private Date fechaCreacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_modificacion", nullable = false)
    private Date fechaModificacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = new Date();
        this.fechaModificacion = new Date();
        this.fechaPago = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = new Date();
    }

    // 🛠️ Método auxiliar: Validar pago
    public void validar(UsuarioEntity usuario) {
        this.estado = EstadoPago.VALIDADO;
        this.fechaValidacion = new Date();
        this.validadoPor = usuario;
    }

    // 🛠️ Método auxiliar: Rechazar pago
    public void rechazar(UsuarioEntity usuario, String motivo) {
        this.estado = EstadoPago.RECHAZADO;
        this.fechaValidacion = new Date();
        this.validadoPor = usuario;
        this.motivoRechazo = motivo;
    }
}