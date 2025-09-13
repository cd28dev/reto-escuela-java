package cd.dev.stockms.service.impl;


import cd.dev.stockms.exception.BadRequestException;
import cd.dev.stockms.exception.CustomBadRequestException;
import cd.dev.stockms.exception.NotFoundException;
import cd.dev.stockms.mapper.StockMapper;
import cd.dev.stockms.model.dto.FindByProductIdDto;
import cd.dev.stockms.model.dto.StockCreateRequestDto;
import cd.dev.stockms.model.dto.StockResponseDto;
import cd.dev.stockms.model.dto.StockUpdateRequestDto;
import cd.dev.stockms.model.entity.Stock;
import cd.dev.stockms.repository.StockRepository;
import cd.dev.stockms.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    @Override
    public StockResponseDto createStock(StockCreateRequestDto request) {
        if (request.getQuantity() < 0) {
            throw new CustomBadRequestException("La cantidad no puede ser negativa");
        }
        Stock stock = stockMapper.toEntity(request);
        Stock saved = stockRepository.save(stock);
        return stockMapper.toResponseDto(saved);
    }

    @Override
    public List<StockResponseDto> createStocks(List<StockCreateRequestDto> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new BadRequestException("La lista de stocks no puede estar vacía");
        }

        List<Stock> entities = requests.stream()
                .map(req -> {
                    if (req.getQuantity() < 0) {
                        throw new CustomBadRequestException(
                                "Cantidad inválida para productId=" + req.getProductId()
                        );
                    }
                    return stockMapper.toEntity(req);
                })
                .toList();

        List<Stock> saved = stockRepository.saveAll(entities);
        return stockMapper.toResponseDtoList(saved);
    }

    @Override
    public StockResponseDto updateStock(Long id, StockUpdateRequestDto request) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stock no encontrado con id " + id));

        if (request.getQuantity() < 0) {
            throw new CustomBadRequestException("La cantidad no puede ser negativa");
        }

        stock.setProductId(request.getProductId());
        stock.setWareHouseId(request.getWareHouseId());
        stock.setQuantity(request.getQuantity());

        Stock updated = stockRepository.save(stock);
        return stockMapper.toResponseDto(updated);
    }

    @Override
    public void deleteStock(Long id) {
        if (!stockRepository.existsById(id)) {
            throw new NotFoundException("Stock no encontrado con id " + id);
        }
        stockRepository.deleteById(id);
    }

    @Override
    public StockResponseDto getById(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stock no encontrado con id " + id));
        return stockMapper.toResponseDto(stock);
    }

    @Override
    public List<StockResponseDto> getAll() {
        return stockMapper.toResponseDtoList(stockRepository.findAll());
    }

    @Override
    public FindByProductIdDto getTotalByProductId(Long productId) {
        List<Stock> stocks = stockRepository.findByProductId(productId);
        if (stocks.isEmpty()) {
            throw new NotFoundException("No existen registros de stock para el producto con id " + productId);
        }

        int total = stocks.stream().mapToInt(Stock::getQuantity).sum();

        if (total <= 0) {
            throw new CustomBadRequestException("Stock agotado para el producto " + productId);
        }

        return new FindByProductIdDto(productId, total);
    }
}