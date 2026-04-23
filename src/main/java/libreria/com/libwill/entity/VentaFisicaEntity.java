package libreria.com.libwill.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import libreria.com.libwill.entity.enums.TipoVenta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "ventas_fisicas")
@NoArgsConstructor
@AllArgsConstructor
public class VentaFisicaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Relación con Usuario (vendedor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id", nullable = false)
    private UsuarioEntity vendedor;

    @NotNull(message = "El tipo de venta no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_venta", nullable = false, length = 20)
    private TipoVenta tipoVenta;

    @NotNull(message = "La fecha de venta no puede ser nula")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_venta", nullable = false)
    private Date fechaVenta;

    @NotNull(message = "El total no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El total debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(length = 20)
    private String estado = "COMPLETADA"; // Siempre COMPLETADA

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // 🔗 Relación con items de la venta
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VentaItemEntity> items = new ArrayList<>();

    // 🔗 Relación con pago (uno a uno)
    @OneToOne(mappedBy = "ventaFisica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PagoEntity pago;

    // 🔗 Relación con boleta (uno a uno, opcional)
    @OneToOne(mappedBy = "ventaFisica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BoletaEntity boleta;

    // 📅 Auditoría
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private Date fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = new Date();
        this.fechaVenta = new Date();
    }

    // 🛠️ Métodos auxiliares
    public void addItem(VentaItemEntity item) {
        items.add(item);
        item.setVenta(this);
    }

    public void removeItem(VentaItemEntity item) {
        items.remove(item);
        item.setVenta(null);
    }

    public void calcularTotal() {
        this.total = items.stream()
                .map(VentaItemEntity::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}