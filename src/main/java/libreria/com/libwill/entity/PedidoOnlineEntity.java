package libreria.com.libwill.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import libreria.com.libwill.entity.enums.EstadoPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "pedidos_online")
@NoArgsConstructor
@AllArgsConstructor
public class PedidoOnlineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Relación con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @NotNull(message = "La fecha del pedido no puede ser nula")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_pedido", nullable = false)
    private Date fechaPedido;

    @NotNull(message = "El total no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El total debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @NotNull(message = "El estado no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPedido estado;

    // 📍 Información de envío
    @NotNull(message = "La dirección de envío no puede ser nula")
    @Size(max = 255)
    @Column(name = "direccion_envio", nullable = false, length = 255)
    private String direccionEnvio;

    @Size(max = 20)
    @Column(name = "numero_casa", length = 20)
    private String numeroCasa;

    @Size(max = 255)
    @Column(name = "referencia", length = 255)
    private String referencia;

    @Size(max = 15)
    @Column(name = "telefono_contacto", length = 15)
    private String telefonoContacto;

    // 📝 Observaciones
    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // 🔗 Relación con items del pedido
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PedidoItemEntity> items = new ArrayList<>();

    // 🔗 Relación con pago (uno a uno)
    @OneToOne(mappedBy = "pedidoOnline", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PagoEntity pago;

    // 🔗 Relación con boleta (uno a uno)
    @OneToOne(mappedBy = "pedidoOnline", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BoletaEntity boleta;

    // 📅 Auditoría
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private Date fechaCreacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_modificacion", nullable = false)
    private Date fechaModificacion;

    @Column(name = "modificado_por", length = 100)
    private String modificadoPor;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = new Date();
        this.fechaModificacion = new Date();
        this.fechaPedido = new Date();
        if (this.estado == null) {
            this.estado = EstadoPedido.PENDIENTE_PAGO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = new Date();
    }

    // 🛠️ Métodos auxiliares
    public void addItem(PedidoItemEntity item) {
        items.add(item);
        item.setPedido(this);
    }

    public void removeItem(PedidoItemEntity item) {
        items.remove(item);
        item.setPedido(null);
    }

    public void calcularTotal() {
        this.total = items.stream()
                .map(PedidoItemEntity::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}