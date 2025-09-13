package cd.dev.stockms.controller;


import cd.dev.stockms.model.dto.FindByProductIdDto;
import cd.dev.stockms.model.dto.StockCreateRequestDto;
import cd.dev.stockms.model.dto.StockResponseDto;
import cd.dev.stockms.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/{productId}")
    public ResponseEntity<FindByProductIdDto> stockByProductId(@PathVariable Long productId) {
        FindByProductIdDto result = stockService.getTotalByProductId(productId);
        if (result == null || result.getTotal() == 0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<List<StockResponseDto>> saveStock(@RequestBody List<StockCreateRequestDto> requests) {
        List<StockResponseDto> result = stockService.createStocks(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(result); // 201
    }
}