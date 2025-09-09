package com.nttdata.dockerized.postgresql.service.impl;

import com.nttdata.dockerized.postgresql.mapper.ProductMapper;
import com.nttdata.dockerized.postgresql.model.dto.ProductSaveRequestDto;
import com.nttdata.dockerized.postgresql.model.dto.ProductSaveResponseDto;
import com.nttdata.dockerized.postgresql.model.entity.Product;
import com.nttdata.dockerized.postgresql.repository.CategoryRepository;
import com.nttdata.dockerized.postgresql.repository.ProductRepository;
import com.nttdata.dockerized.postgresql.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<ProductDto> listAll() {
        List<Product> products = (List<Product>) productRepository.findAll();
        return ProductMapper.INSTANCE.map(products);
    }

    @Override
    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        return ProductMapper.INSTANCE.map(product);
    }

    @Override
    @Transactional
    public ProductSaveResponseDto save(ProductSaveRequestDto request) {
        validateProductSaveRequest(request);

        // Verificar que la categoría existe
        if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + request.getCategoryId()));
        }

        Product product = ProductMapper.INSTANCE.toEntity(request);

        // Establecer valores por defecto si no se proporcionan
        if (product.getActive() == null) {
            product.setActive(true);
        }

        Product savedProduct = productRepository.save(product);
        return ProductMapper.INSTANCE.toProductSaveResponseDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductDto update(Long id, ProductSaveRequestDto request) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        // Verificar que la nueva categoría existe si se proporciona
        if (request.getCategoryId() != null && !request.getCategoryId().equals(existingProduct.getCategory().getId())) {
            categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + request.getCategoryId()));
        }

        Product productToUpdate = ProductMapper.INSTANCE.toEntityForUpdate(id, request, existingProduct);
        Product updatedProduct = productRepository.save(productToUpdate);
        return ProductMapper.INSTANCE.map(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productRepository.deleteById(id);
    }

    private void validateProductSaveRequest(ProductSaveRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser null");
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (request.getCategoryId() == null) {
            throw new IllegalArgumentException("El ID de categoría no puede ser null");
        }
    }
}
