package libreria.com.libwill.controller;

import libreria.com.libwill.dto.request.RechazarPagoDTO;
import libreria.com.libwill.dto.request.ValidarPagoDTO;
import libreria.com.libwill.dto.response.PagoResponseDTO;
import libreria.com.libwill.entity.enums.EstadoPago;
import libreria.com.libwill.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PagoController {

    private final PagoService pagoService;

    // ========= CLIENTE: SUBIR COMPROBANTE =========

    @PostMapping(
            value = "/{pedidoId}/comprobante",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PagoResponseDTO> subirComprobante(
            @PathVariable Long pedidoId,
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam("numeroOperacion") String numeroOperacion,
            Principal principal
    ) {

        if (archivo == null || archivo.isEmpty()) {
            throw new RuntimeException("El archivo de comprobante viene vacío.");
        }

        String username = principal.getName();
        PagoResponseDTO response = pagoService.subirComprobante(
                pedidoId,
                archivo,
                numeroOperacion,
                username
        );
        return ResponseEntity.ok(response);
    }

    // ========= CLIENTE / ADMIN: VER PAGO POR PEDIDO =========

    @GetMapping("/pedido/{pedidoId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PagoResponseDTO> obtenerPagoPorPedido(@PathVariable Long pedidoId) {
        PagoResponseDTO response = pagoService.obtenerPagoPorPedidoOnline(pedidoId);
        return ResponseEntity.ok(response);
    }

    // ========= ADMIN / VENDEDOR: VALIDAR PAGO =========

    @PutMapping("/{pagoId}/validar")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<PagoResponseDTO> validarPago(
            @PathVariable Long pagoId,
            @RequestBody ValidarPagoDTO dto,
            Principal principal
    ) {
        String username = principal.getName();
        PagoResponseDTO response = pagoService.validarPago(pagoId, dto, username);
        return ResponseEntity.ok(response);
    }

    // ========= ADMIN / VENDEDOR: RECHAZAR PAGO =========

    @PutMapping("/{pagoId}/rechazar")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<PagoResponseDTO> rechazarPago(
            @PathVariable Long pagoId,
            @RequestBody RechazarPagoDTO dto,
            Principal principal
    ) {
        String username = principal.getName();
        PagoResponseDTO response = pagoService.rechazarPago(pagoId, dto, username);
        return ResponseEntity.ok(response);
    }

    // ========= ADMIN / VENDEDOR: LISTAR PENDIENTES =========

    @GetMapping("/pendientes")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPagosPendientes() {
        List<PagoResponseDTO> list = pagoService.obtenerPagosPendientes();
        return ResponseEntity.ok(list);
    }

    // ========= ADMIN / VENDEDOR: VER POR ID =========

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<PagoResponseDTO> obtenerPagoPorId(@PathVariable Long id) {
        PagoResponseDTO response = pagoService.obtenerPagoPorId(id);
        return ResponseEntity.ok(response);
    }

    // ========= ADMIN / VENDEDOR: LISTAR POR ESTADO =========

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPagosPorEstado(@PathVariable String estado) {
        EstadoPago est = EstadoPago.valueOf(estado);
        List<PagoResponseDTO> list = pagoService.obtenerPagosPorEstado(est);
        return ResponseEntity.ok(list);
    }
}
