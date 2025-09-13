package cd.dev.compositionorder.service.impl;
import cd.dev.compositionorder.dto.external.*;
import cd.dev.compositionorder.feign.PedidoServiceClient;
import cd.dev.compositionorder.feign.ProductServiceClient;
import cd.dev.compositionorder.feign.StockClient;
import cd.dev.compositionorder.feign.UserServiceClient;
import cd.dev.compositionorder.service.OrderCompositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderCompositionServiceImpl implements OrderCompositionService {
    @Autowired
    private UserServiceClient userClient;
    @Autowired
    private ProductServiceClient productClient;
    @Autowired
    private PedidoServiceClient pedidoClient;
    @Autowired
    private StockClient stockClient;

    @Override
    public UserResponseDto getUserById(Long id) {
        return userClient.getUserById(id);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userClient.getAllUsers();
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        return productClient.getProductById(id);
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productClient.getAllProducts();
    }

    @Override
    public PedidoResponseDto getPedidoById(Long id) {
        return pedidoClient.getPedidoById(id);
    }

    @Override
    public List<PedidoResponseDto> getAllPedidos() {
        return pedidoClient.getAllPedidos();
    }

    @Override
    public PedidoResponseDto createPedido(PedidoCreateRequestDto request) {
        return pedidoClient.createPedido(request);
    }

    @Override
    public PedidoResponseDto addDetalle(Long pedidoId, DetallePedidoCreateRequestDto detalleDto) {
        return pedidoClient.addDetalle(pedidoId, detalleDto);
    }

    @Override
    public void deletePedido(Long id) {
        pedidoClient.deletePedido(id);
    }

    @Override
    public FindByProductIdDto getTotalByProductId(Long productId) {
        return stockClient.getTotalByProductId(productId);
    }

    @Override
    public List<StockResponseDto> createStocks(List<StockCreateRequestDto> requests) {
        return stockClient.createStocks(requests);
    }
}
