package cd.dev.compositionorder.controller;

import cd.dev.compositionorder.service.OrderCompositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import cd.dev.compositionorder.dto.external.*;

@RestController
@RequestMapping("/api/composition")
@RequiredArgsConstructor
public class OrderCompositionController {

    private final OrderCompositionService orderCompositionService;

    // ---- USERS ----
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(orderCompositionService.getUserById(id));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(orderCompositionService.getAllUsers());
    }

    // ---- PRODUCTS ----
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(orderCompositionService.getProductById(id));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(orderCompositionService.getAllProducts());
    }

    // ---- PEDIDOS ----
    @GetMapping("/pedidos/{id}")
    public ResponseEntity<PedidoResponseDto> getPedidoById(@PathVariable Long id) {
        return ResponseEntity.ok(orderCompositionService.getPedidoById(id));
    }

    @GetMapping("/pedidos")
    public ResponseEntity<List<PedidoResponseDto>> getAllPedidos() {
        return ResponseEntity.ok(orderCompositionService.getAllPedidos());
    }

    @PostMapping("/pedidos")
    public ResponseEntity<PedidoResponseDto> createPedido(@RequestBody PedidoCreateRequestDto request) {
        return ResponseEntity.ok(orderCompositionService.createPedido(request));
    }

    @PostMapping("/pedidos/{pedidoId}/detalles")
    public ResponseEntity<PedidoResponseDto> addDetalle(
            @PathVariable Long pedidoId,
            @RequestBody DetallePedidoCreateRequestDto detalleDto) {
        return ResponseEntity.ok(orderCompositionService.addDetalle(pedidoId, detalleDto));
    }

    @DeleteMapping("/pedidos/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id) {
        orderCompositionService.deletePedido(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stocks/{productId}")
    public ResponseEntity<FindByProductIdDto> getTotalByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(orderCompositionService.getTotalByProductId(productId));
    }

    @PostMapping("/stocks")
    public ResponseEntity<List<StockResponseDto>> createStocks(
            @RequestBody List<StockCreateRequestDto> requests) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderCompositionService.createStocks(requests));
    }
}