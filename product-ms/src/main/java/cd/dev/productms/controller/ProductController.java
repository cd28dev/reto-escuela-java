package cd.dev.productms.controller;

import cd.dev.productms.model.dto.ProductCreateRequestDto;
import cd.dev.productms.model.dto.ProductResponseDto;
import cd.dev.productms.model.dto.ProductUpdateRequestDto;
import cd.dev.productms.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponseDto> getAllProducts() {
        return productService.listAll();
    }

    @GetMapping("/{id}")
    public ProductResponseDto getProductById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    public ProductResponseDto createProduct(@Valid @RequestBody ProductCreateRequestDto request) {
        return productService.save(request);
    }

    @PutMapping("/{id}")
    public ProductResponseDto updateProduct(
            @PathVariable Long id,
            @RequestBody ProductUpdateRequestDto request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
    }