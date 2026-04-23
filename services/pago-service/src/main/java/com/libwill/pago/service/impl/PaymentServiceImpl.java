package com.libwill.pago.service.impl;

import com.libwill.pago.dto.PaymentDTO;
import com.libwill.pago.entity.PaymentEntity;
import com.libwill.pago.exception.NotFoundException;
import com.libwill.pago.repository.PaymentRepository;
import com.libwill.pago.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;

    @Override
    public List<PaymentDTO> getAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public PaymentDTO getById(Long id) {
        return toDto(findEntity(id));
    }

    @Override
    public List<PaymentDTO> getBySaleCode(String saleCode) {
        return repository.findBySaleCode(saleCode).stream().map(this::toDto).toList();
    }

    @Override
    public PaymentDTO create(PaymentDTO dto) {
        if (repository.existsByPaymentCode(dto.getPaymentCode())) {
            throw new RuntimeException("Ya existe un pago con ese codigo");
        }
        return toDto(repository.save(toEntity(dto)));
    }

    @Override
    public PaymentDTO update(Long id, PaymentDTO dto) {
        PaymentEntity entity = findEntity(id);
        entity.setPaymentCode(dto.getPaymentCode());
        entity.setSaleCode(dto.getSaleCode());
        entity.setAmount(dto.getAmount());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setStatus(dto.getStatus());
        entity.setPaidAt(dto.getPaidAt());
        return toDto(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        PaymentEntity entity = findEntity(id);
        repository.delete(entity);
    }

    private PaymentEntity findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pago no encontrado con id " + id));
    }

    private PaymentDTO toDto(PaymentEntity entity) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(entity.getId());
        dto.setPaymentCode(entity.getPaymentCode());
        dto.setSaleCode(entity.getSaleCode());
        dto.setAmount(entity.getAmount());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setStatus(entity.getStatus());
        dto.setPaidAt(entity.getPaidAt());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private PaymentEntity toEntity(PaymentDTO dto) {
        PaymentEntity entity = new PaymentEntity();
        entity.setPaymentCode(dto.getPaymentCode());
        entity.setSaleCode(dto.getSaleCode());
        entity.setAmount(dto.getAmount());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setStatus(dto.getStatus());
        entity.setPaidAt(dto.getPaidAt());
        return entity;
    }
}
