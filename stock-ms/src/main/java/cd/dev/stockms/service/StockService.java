package cd.dev.stockms.service;

import cd.dev.stockms.model.dto.FindByProductIdDto;
import cd.dev.stockms.model.dto.StockCreateRequestDto;
import cd.dev.stockms.model.dto.StockResponseDto;
import cd.dev.stockms.model.dto.StockUpdateRequestDto;

import java.util.List;

public interface StockService {

    StockResponseDto createStock(StockCreateRequestDto request);

    List<StockResponseDto> createStocks(List<StockCreateRequestDto> requests);

    StockResponseDto updateStock(Long id, StockUpdateRequestDto request);

    void deleteStock(Long id);

    StockResponseDto getById(Long id);

    List<StockResponseDto> getAll();

    FindByProductIdDto getTotalByProductId(Long productId);
}
