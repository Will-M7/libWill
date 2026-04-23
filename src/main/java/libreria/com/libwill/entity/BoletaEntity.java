package libreria.com.libwill.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import libreria.com.libwill.entity.enums.TipoComprobante;
import libreria.com.libwill.entity.enums.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "boletas")
@NoArgsConstructor
@AllArgsConstructor
public class BoletaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El tipo de comprobante no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_comprobante", nullable = false, length = 20)
    private TipoComprobante tipoComprobante;

    @NotNull(message = "La serie no puede ser nula")
    @Size(max = 10)
    @Column(nullable = false, length = 10)
    private String serie; // Ej: B001, F001

    @NotNull(message = "El número no puede ser nulo")
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String numero; // Ej: 00001, 00002

    @Column(name = "numero_completo", length = 30)
    private String numeroCompleto; // B001-00001 (calculado)

    @NotNull(message = "La fecha de emisión no puede ser nula")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_emision", nullable = false)
    private Date fechaEmision;

    @NotNull(message = "El tipo de documento del cliente no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento_cliente", nullable = false, length = 10)
    private TipoDocumento tipoDocumentoCliente;

    @NotNull(message = "El número de documento no puede ser nulo")
    @Size(max = 20)
    @Column(name = "numero_documento", nullable = false, length = 20)
    private String numeroDocumento;

    @NotNull(message = "El nombre del cliente no puede ser nulo")
    @Size(max = 200)
    @Column(name = "nombre_cliente", nullable = false, length = 200)
    private String nombreCliente;

    @Size(max = 255)
    @Column(name = "direccion_cliente", length = 255)
    private String direccionCliente;

    @NotNull
    @DecimalMin("0.00")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @NotNull
    @DecimalMin("0.00")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal igv; // 18%

    @NotNull
    @DecimalMin("0.01")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    // 🔗 Relación con PedidoOnline (puede ser null)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_online_id")
    private PedidoOnlineEntity pedidoOnline;

    // 🔗 Relación con VentaFisica (puede ser null)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_fisica_id")
    private VentaFisicaEntity ventaFisica;

    @Column(name = "url_pdf", length = 255)
    private String urlPdf;

    // 📅 Auditoría
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private Date fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = new Date();
        this.fechaEmision = new Date();
        this.numeroCompleto = this.serie + "-" + this.numero;
    }

    // 🛠️ Método auxiliar: Calcular IGV y total
    public void calcularMontos(BigDecimal subtotalSinIgv) {
        this.subtotal = subtotalSinIgv;
        this.igv = subtotalSinIgv.multiply(new BigDecimal("0.18")); // 18%
        this.total = subtotalSinIgv.add(this.igv);
    }
}