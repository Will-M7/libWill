package com.libwill.venta.repository;

import com.libwill.venta.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<SaleEntity, Long> {
    boolean existsBySaleCode(String saleCode);
}
