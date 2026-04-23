package com.libwill.pago.config;

import com.libwill.pago.entity.PaymentEntity;
import com.libwill.pago.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SeedDataConfig {

    private final PaymentRepository repository;

    @Bean
    CommandLineRunner seedPaymentData() {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            PaymentEntity pago1 = new PaymentEntity();
            pago1.setPaymentCode("PAG-001");
            pago1.setSaleCode("VEN-001");
            pago1.setAmount(new BigDecimal("35.70"));
            pago1.setPaymentMethod("EFECTIVO");
            pago1.setStatus("COMPLETADO");
            pago1.setPaidAt(new Date());

            PaymentEntity pago2 = new PaymentEntity();
            pago2.setPaymentCode("PAG-002");
            pago2.setSaleCode("VEN-002");
            pago2.setAmount(new BigDecimal("82.40"));
            pago2.setPaymentMethod("YAPE");
            pago2.setStatus("PENDIENTE");
            pago2.setPaidAt(new Date());

            repository.saveAll(List.of(pago1, pago2));
        };
    }
}
