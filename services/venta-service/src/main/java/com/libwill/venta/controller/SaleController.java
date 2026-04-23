package com.libwill.venta.controller;

import com.libwill.venta.dto.SaleDTO;
import com.libwill.venta.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SaleController {

    private final SaleService service;

    @GetMapping
    public ResponseEntity<List<SaleDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/codigo/{saleCode}")
    public ResponseEntity<SaleDTO> getBySaleCode(@PathVariable String saleCode) {
        return ResponseEntity.ok(service.getBySaleCode(saleCode));
    }

    @PostMapping
    public ResponseEntity<SaleDTO> create(@Valid @RequestBody SaleDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleDTO> update(@PathVariable Long id, @Valid @RequestBody SaleDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
