package libreria.com.libwill.controller;

import jakarta.validation.Valid;
import libreria.com.libwill.dto.request.CreateVentaFisicaDTO;
import libreria.com.libwill.dto.response.VentaFisicaResponseDTO;
import libreria.com.libwill.service.VentaFisicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas-fisicas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class VentaFisicaController {

    private final VentaFisicaService service;

    // Crear venta física (vendedor)
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<VentaFisicaResponseDTO> crearVenta(
            @Valid @RequestBody CreateVentaFisicaDTO dto,
            Authentication authentication) {
        String username = authentication.getName();
        return new ResponseEntity<>(service.crearVenta(dto, username), HttpStatus.CREATED);
    }

    // Mis ventas
    @GetMapping("/mis-ventas")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<List<VentaFisicaResponseDTO>> misVentas(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(service.obtenerMisVentas(username));
    }

    // Venta por ID
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<VentaFisicaResponseDTO> obtenerPorId(
            @PathVariable Long id,
            Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(service.obtenerVentaPorId(id, username));
    }

    // Todas las ventas (admin)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VentaFisicaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(service.obtenerTodasLasVentas());
    }

    // Por vendedor
    @GetMapping("/vendedor/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VentaFisicaResponseDTO>> porVendedor(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerVentasPorVendedor(id));
    }
}