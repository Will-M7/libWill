package com.libwill.inventario.service.impl;

import com.libwill.inventario.dto.InventoryMovementDTO;
import com.libwill.inventario.entity.InventoryMovementEntity;
import com.libwill.inventario.exception.NotFoundException;
import com.libwill.inventario.repository.InventoryMovementRepository;
import com.libwill.inventario.service.InventoryMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryMovementServiceImpl implements InventoryMovementService {

    private final InventoryMovementRepository repository;

    @Override
    public List<InventoryMovementDTO> getAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public InventoryMovementDTO getById(Long id) {
        return toDto(findEntity(id));
    }

    @Override
    public InventoryMovementDTO create(InventoryMovementDTO dto) {
        return toDto(repository.save(toEntity(dto)));
    }

    @Override
    public InventoryMovementDTO update(Long id, InventoryMovementDTO dto) {
        InventoryMovementEntity entity = findEntity(id);
        entity.setProductSku(dto.getProductSku());
        entity.setMovementType(dto.getMovementType());
        entity.setQuantity(dto.getQuantity());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setNote(dto.getNote());
        entity.setMovementDate(dto.getMovementDate());
        return toDto(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        InventoryMovementEntity entity = findEntity(id);
        repository.delete(entity);
    }

    private InventoryMovementEntity findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movimiento de inventario no encontrado con id " + id));
    }

    private InventoryMovementDTO toDto(InventoryMovementEntity entity) {
        InventoryMovementDTO dto = new InventoryMovementDTO();
        dto.setId(entity.getId());
        dto.setProductSku(entity.getProductSku());
        dto.setMovementType(entity.getMovementType());
        dto.setQuantity(entity.getQuantity());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setNote(entity.getNote());
        dto.setMovementDate(entity.getMovementDate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private InventoryMovementEntity toEntity(InventoryMovementDTO dto) {
        InventoryMovementEntity entity = new InventoryMovementEntity();
        entity.setProductSku(dto.getProductSku());
        entity.setMovementType(dto.getMovementType());
        entity.setQuantity(dto.getQuantity());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setNote(dto.getNote());
        entity.setMovementDate(dto.getMovementDate());
        return entity;
    }
}
