package com.libwill.venta.config;

import com.libwill.venta.entity.SaleEntity;
import com.libwill.venta.repository.SaleRepository;
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

    private final SaleRepository repository;

    @Bean
    CommandLineRunner seedSaleData() {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            SaleEntity venta1 = new SaleEntity();
            venta1.setSaleCode("VEN-001");
            venta1.setCustomerName("Cliente Demo 1");
            venta1.setTotalAmount(new BigDecimal("35.70"));
            venta1.setStatus("PAGADA");
            venta1.setPaymentMethod("EFECTIVO");
            venta1.setSaleDate(new Date());

            SaleEntity venta2 = new SaleEntity();
            venta2.setSaleCode("VEN-002");
            venta2.setCustomerName("Cliente Demo 2");
            venta2.setTotalAmount(new BigDecimal("82.40"));
            venta2.setStatus("PENDIENTE");
            venta2.setPaymentMethod("YAPE");
            venta2.setSaleDate(new Date());

            repository.saveAll(List.of(venta1, venta2));
        };
    }
}
