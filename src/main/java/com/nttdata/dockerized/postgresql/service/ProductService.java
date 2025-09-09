package com.nttdata.dockerized.postgresql.service;

import com.nttdata.dockerized.postgresql.model.dto.*;

import java.util.List;

public interface ProductService {
    List<ProductResponseDto> listAll();
    ProductResponseDto findById(Long id);
    ProductResponseDto save(ProductCreateRequestDto request);
    ProductResponseDto update(Long id, ProductUpdateRequestDto request);
    void deleteById(Long id);
}
