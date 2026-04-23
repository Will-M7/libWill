package com.libwill.venta.service;

import com.libwill.venta.dto.SaleDTO;

import java.util.List;

public interface SaleService {
    List<SaleDTO> getAll();
    SaleDTO getById(Long id);
    SaleDTO create(SaleDTO dto);
    SaleDTO update(Long id, SaleDTO dto);
    void delete(Long id);
}
