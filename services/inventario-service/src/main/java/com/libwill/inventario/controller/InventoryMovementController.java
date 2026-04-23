package com.libwill.inventario.controller;

import com.libwill.inventario.dto.InventoryMovementDTO;
import com.libwill.inventario.service.InventoryMovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InventoryMovementController {

    private final InventoryMovementService service;

    @GetMapping
    public ResponseEntity<List<InventoryMovementDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryMovementDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<InventoryMovementDTO> create(@Valid @RequestBody InventoryMovementDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryMovementDTO> update(@PathVariable Long id, @Valid @RequestBody InventoryMovementDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
