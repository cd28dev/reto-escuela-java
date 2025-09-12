package cd.dev.compositionorder.service;

import cd.dev.compositionorder.dto.OrderCompositionCreateRequestDto;
import cd.dev.compositionorder.dto.OrderCompositionRequestDto;
import cd.dev.compositionorder.dto.OrderCompositionResponseDto;

import java.util.List;

public interface OrderCompositionService {
    OrderCompositionResponseDto createCompleteOrder(OrderCompositionRequestDto request);
    OrderCompositionResponseDto getOrderComposition(Long pedidoId);
    List<OrderCompositionResponseDto> getAllOrdersComposition();
    List<OrderCompositionResponseDto> getOrdersByUser(Long userId);
    void cancelOrder(Long pedidoId);
    OrderCompositionResponseDto getOrderSummary(Long pedidoId);
}
