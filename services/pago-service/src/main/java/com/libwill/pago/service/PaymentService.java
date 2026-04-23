package com.libwill.pago.service;

import com.libwill.pago.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {
    List<PaymentDTO> getAll();
    PaymentDTO getById(Long id);
    PaymentDTO create(PaymentDTO dto);
    PaymentDTO update(Long id, PaymentDTO dto);
    void delete(Long id);
}
