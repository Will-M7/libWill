package libreria.com.libwill.controller;

import libreria.com.libwill.dto.response.ReporteVentasDTO;
import libreria.com.libwill.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReporteController {

    private final ReporteService service;

    @GetMapping("/general")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReporteVentasDTO> reporteGeneral(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin) {

        return ResponseEntity.ok(service.obtenerReporteGeneral(inicio, fin));
    }

    @GetMapping("/mas-vendidos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> masVendidos(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin) {

        return ResponseEntity.ok(service.obtenerProductosMasVendidos(inicio, fin));
    }

    @GetMapping("/ranking-vendedores")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> ranking(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin) {

        return ResponseEntity.ok(service.obtenerRankingVendedores(inicio, fin));
    }

    @GetMapping("/metodo-pago")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> ventasPorMetodo(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin) {

        return ResponseEntity.ok(service.obtenerVentasPorMetodoPago(inicio, fin));
    }
}
