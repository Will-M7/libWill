package com.libwill.pago.repository;

import com.libwill.pago.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    boolean existsByPaymentCode(String paymentCode);
    List<PaymentEntity> findBySaleCode(String saleCode);
}
