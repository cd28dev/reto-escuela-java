package cd.dev.compositionorder.service;
import cd.dev.compositionorder.dto.external.*;

import java.util.List;

public interface OrderCompositionService {

    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getAllUsers();

    ProductResponseDto getProductById(Long id);
    List<ProductResponseDto> getAllProducts();

    PedidoResponseDto getPedidoById(Long id);

    List<PedidoResponseDto> getAllPedidos();

    PedidoResponseDto createPedido(PedidoCreateRequestDto request);

    PedidoResponseDto addDetalle(Long pedidoId,DetallePedidoCreateRequestDto detalleDto);

    void deletePedido(Long id);

    // --- STOCK (solo lo que el API expone) ---
    FindByProductIdDto getTotalByProductId(Long productId);
    List<StockResponseDto> createStocks(List<StockCreateRequestDto> requests);
}
