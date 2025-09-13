package cd.dev.compositionorder.feign;


import cd.dev.compositionorder.dto.external.FindByProductIdDto;
import cd.dev.compositionorder.dto.external.StockCreateRequestDto;
import cd.dev.compositionorder.dto.external.StockResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "stock-ms", url = "${services.stock-ms.url}")
public interface StockClient {

    @GetMapping("/api/stock/{productId}")
    FindByProductIdDto getTotalByProductId(@PathVariable("productId") Long productId);

    @PostMapping("/api/stock")
    List<StockResponseDto> createStocks(@RequestBody List<StockCreateRequestDto> request);

}