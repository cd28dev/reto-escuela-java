package com.nttdata.dockerized.postgresql.controller;

import com.nttdata.dockerized.postgresql.model.dto.ProductSaveRequestDto;
import com.nttdata.dockerized.postgresql.model.dto.ProductSaveResponseDto;
import com.nttdata.dockerized.postgresql.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.listAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        try {
            ProductDto product = productService.findById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductSaveRequestDto request) {
        try {
            ProductSaveResponseDto savedProduct = productService.save(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @RequestBody ProductSaveRequestDto request) {
        try {
            ProductDto updatedProduct = productService.update(id, request);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable Long categoryId) {
        try {
            List<ProductDto> products = productService.listAll()
                    .stream()
                    .filter(product -> product.getCategory() != null &&
                            categoryId.equals(product.getCategory().getId()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/price/{price}")
    public ResponseEntity<List<ProductDto>> getProductsByPrice(@PathVariable BigDecimal price) {
        try {
            List<ProductDto> products = productService.listAll()
                    .stream()
                    .filter(product -> product.getPrice().compareTo(price) == 0)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/price")
    public ResponseEntity<List<ProductDto>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        try {
            List<ProductDto> products = productService.listAll()
                    .stream()
                    .filter(product -> {
                        BigDecimal productPrice = product.getPrice();
                        boolean minCondition = minPrice == null || productPrice.compareTo(minPrice) >= 0;
                        boolean maxCondition = maxPrice == null || productPrice.compareTo(maxPrice) <= 0;
                        return minCondition && maxCondition;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
