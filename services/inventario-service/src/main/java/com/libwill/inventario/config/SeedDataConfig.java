package com.libwill.inventario.config;

import com.libwill.inventario.entity.InventoryMovementEntity;
import com.libwill.inventario.repository.InventoryMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SeedDataConfig {

    private final InventoryMovementRepository repository;

    @Bean
    CommandLineRunner seedInventoryData() {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            InventoryMovementEntity entrada = new InventoryMovementEntity();
            entrada.setProductSku("LIB-CUA-A4-001");
            entrada.setMovementType("ENTRADA");
            entrada.setQuantity(100);
            entrada.setReferenceCode("INI-INV-001");
            entrada.setNote("Carga inicial de inventario");
            entrada.setMovementDate(new Date());

            InventoryMovementEntity salida = new InventoryMovementEntity();
            salida.setProductSku("LIB-LAP-AZ-001");
            salida.setMovementType("SALIDA");
            salida.setQuantity(15);
            salida.setReferenceCode("INI-INV-002");
            salida.setNote("Salida inicial por demostracion");
            salida.setMovementDate(new Date());

            repository.saveAll(List.of(entrada, salida));
        };
    }
}
