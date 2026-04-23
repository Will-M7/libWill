package com.libwill.inventario.repository;

import com.libwill.inventario.entity.InventoryMovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovementEntity, Long> {
}
