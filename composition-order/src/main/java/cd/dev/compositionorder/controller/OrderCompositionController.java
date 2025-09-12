package cd.dev.compositionorder.controller;

import cd.dev.compositionorder.dto.OrderCompositionRequestDto;
import cd.dev.compositionorder.dto.OrderCompositionResponseDto;
import cd.dev.compositionorder.service.OrderCompositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/composition/orders")
@RequiredArgsConstructor
public class OrderCompositionController {

    private final OrderCompositionService orderCompositionService;

    @PostMapping
    public ResponseEntity<OrderCompositionResponseDto> createCompleteOrder(
            @Valid @RequestBody OrderCompositionRequestDto request) {
        OrderCompositionResponseDto response = orderCompositionService.createCompleteOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{pedidoId}")
    public ResponseEntity<OrderCompositionResponseDto> getOrderComposition(@PathVariable Long pedidoId) {
        OrderCompositionResponseDto response = orderCompositionService.getOrderComposition(pedidoId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderCompositionResponseDto>> getAllOrdersComposition() {
        List<OrderCompositionResponseDto> responses = orderCompositionService.getAllOrdersComposition();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderCompositionResponseDto>> getOrdersByUser(@PathVariable Long userId) {
        List<OrderCompositionResponseDto> responses = orderCompositionService.getOrdersByUser(userId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{pedidoId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long pedidoId) {
        orderCompositionService.cancelOrder(pedidoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{pedidoId}/summary")
    public ResponseEntity<OrderCompositionResponseDto> getOrderSummary(@PathVariable Long pedidoId) {
        OrderCompositionResponseDto response = orderCompositionService.getOrderSummary(pedidoId);
        return ResponseEntity.ok(response);
    }
}
