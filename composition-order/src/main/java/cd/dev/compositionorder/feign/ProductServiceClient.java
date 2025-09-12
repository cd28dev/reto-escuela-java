package cd.dev.compositionorder.feign;

import cd.dev.compositionorder.dto.external.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "product-service", url = "${services.product-ms.url}")
public interface ProductServiceClient {

    @GetMapping("/api/products/{id}")
    ProductResponseDto getProductById(@PathVariable Long id);

    @GetMapping("/api/products")
    List<ProductResponseDto> getAllProducts();
}