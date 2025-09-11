package cd.dev.productms.service;

import cd.dev.productms.model.dto.ProductCreateRequestDto;
import cd.dev.productms.model.dto.ProductResponseDto;
import cd.dev.productms.model.dto.ProductUpdateRequestDto;

import java.util.List;

public interface ProductService {
    List<ProductResponseDto> listAll();
    ProductResponseDto findById(Long id);
    ProductResponseDto save(ProductCreateRequestDto request);
    ProductResponseDto update(Long id, ProductUpdateRequestDto request);
    void deleteById(Long id);
}
