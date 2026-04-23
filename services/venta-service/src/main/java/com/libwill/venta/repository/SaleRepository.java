package com.libwill.venta.repository;

import com.libwill.venta.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SaleRepository extends JpaRepository<SaleEntity, Long> {
    boolean existsBySaleCode(String saleCode);
    Optional<SaleEntity> findBySaleCode(String saleCode);
}
