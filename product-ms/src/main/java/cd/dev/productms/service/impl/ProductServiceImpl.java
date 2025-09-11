package cd.dev.productms.service.impl;


import cd.dev.productms.exception.NotFoundException;
import cd.dev.productms.mapper.ProductMapper;
import cd.dev.productms.model.dto.ProductCreateRequestDto;
import cd.dev.productms.model.dto.ProductResponseDto;
import cd.dev.productms.model.entity.Product;
import cd.dev.productms.repository.ProductRepository;
import cd.dev.productms.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> listAll() {
        List<Product> products = productRepository.findAll();
        return productMapper.toResponseList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto con id " + id + " no encontrado"));
        return productMapper.toResponseDto(product);
    }

    @Override
    public ProductResponseDto save(ProductCreateRequestDto request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Categoría con id " + request.getCategoryId() + " no encontrada"));

        Product product = productMapper.toEntity(request);
        product.setCategory(category);

        Product saved = productRepository.save(product);
        return productMapper.toResponseDto(saved);
    }

    @Override
    public ProductResponseDto update(Long id, ProductUpdateRequestDto request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto con id " + id + " no encontrado"));

        // Actualiza solo campos no nulos del DTO
        productMapper.updateEntityFromDto(request, product);

        // Actualiza la categoría solo si viene en el DTO
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Categoría con id " + request.getCategoryId() + " no encontrada"));
            product.setCategory(category);
        }

        Product updated = productRepository.save(product);
        return productMapper.toResponseDto(updated);
    }



    @Override
    public void deleteById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto con id " + id + " no encontrado"));
        productRepository.delete(product);
    }
}