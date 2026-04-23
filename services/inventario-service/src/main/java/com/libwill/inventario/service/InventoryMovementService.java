package com.libwill.inventario.service;

import com.libwill.inventario.dto.InventoryMovementDTO;

import java.util.List;

public interface InventoryMovementService {
    List<InventoryMovementDTO> getAll();
    InventoryMovementDTO getById(Long id);
    InventoryMovementDTO create(InventoryMovementDTO dto);
    InventoryMovementDTO update(Long id, InventoryMovementDTO dto);
    void delete(Long id);
}
