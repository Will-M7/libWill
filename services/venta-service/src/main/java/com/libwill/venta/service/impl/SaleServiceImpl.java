package com.libwill.venta.service.impl;

import com.libwill.venta.dto.SaleDTO;
import com.libwill.venta.entity.SaleEntity;
import com.libwill.venta.exception.NotFoundException;
import com.libwill.venta.repository.SaleRepository;
import com.libwill.venta.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository repository;

    @Override
    public List<SaleDTO> getAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public SaleDTO getById(Long id) {
        return toDto(findEntity(id));
    }

    @Override
    public SaleDTO getBySaleCode(String saleCode) {
        SaleEntity entity = repository.findBySaleCode(saleCode)
                .orElseThrow(() -> new NotFoundException("Venta no encontrada con codigo " + saleCode));
        return toDto(entity);
    }

    @Override
    public SaleDTO create(SaleDTO dto) {
        if (repository.existsBySaleCode(dto.getSaleCode())) {
            throw new RuntimeException("Ya existe una venta con ese codigo");
        }
        return toDto(repository.save(toEntity(dto)));
    }

    @Override
    public SaleDTO update(Long id, SaleDTO dto) {
        SaleEntity entity = findEntity(id);
        entity.setSaleCode(dto.getSaleCode());
        entity.setCustomerName(dto.getCustomerName());
        entity.setTotalAmount(dto.getTotalAmount());
        entity.setStatus(dto.getStatus());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setSaleDate(dto.getSaleDate());
        return toDto(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        SaleEntity entity = findEntity(id);
        repository.delete(entity);
    }

    private SaleEntity findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venta no encontrada con id " + id));
    }

    private SaleDTO toDto(SaleEntity entity) {
        SaleDTO dto = new SaleDTO();
        dto.setId(entity.getId());
        dto.setSaleCode(entity.getSaleCode());
        dto.setCustomerName(entity.getCustomerName());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setStatus(entity.getStatus());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setSaleDate(entity.getSaleDate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private SaleEntity toEntity(SaleDTO dto) {
        SaleEntity entity = new SaleEntity();
        entity.setSaleCode(dto.getSaleCode());
        entity.setCustomerName(dto.getCustomerName());
        entity.setTotalAmount(dto.getTotalAmount());
        entity.setStatus(dto.getStatus());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setSaleDate(dto.getSaleDate());
        return entity;
    }
}
