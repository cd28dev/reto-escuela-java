package cd.dev.compositionorder.service.impl;

import cd.dev.compositionorder.dto.OrderCompositionRequestDto;
import cd.dev.compositionorder.dto.OrderCompositionResponseDto;
import cd.dev.compositionorder.dto.ProductOrderDto;
import cd.dev.compositionorder.dto.external.*;
import cd.dev.compositionorder.feign.PedidoServiceClient;
import cd.dev.compositionorder.feign.ProductServiceClient;
import cd.dev.compositionorder.feign.UserServiceClient;
import cd.dev.compositionorder.service.OrderCompositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCompositionServiceImpl implements OrderCompositionService {

    private final UserServiceClient userClient;
    private final ProductServiceClient productClient;
    private final PedidoServiceClient pedidoClient;

    @Override
    @Transactional
    public OrderCompositionResponseDto createCompleteOrder(OrderCompositionRequestDto request) {
        UserResponseDto user = userClient.getUserById(request.getUserId());
        List<ProductResponseDto> validatedProducts = request.getProductos().stream()
                .map(p -> productClient.getProductById(p.getProductoId()))
                .toList();

        PedidoCreateRequestDto pedidoRequest = new PedidoCreateRequestDto();
        pedidoRequest.setUserId(request.getUserId());
        pedidoRequest.setActive(true);

        PedidoResponseDto pedido = pedidoClient.createPedido(pedidoRequest);

        List<OrderCompositionResponseDto.ProductDetailDto> productDetails = new ArrayList<>();
        BigDecimal totalCalculado = BigDecimal.ZERO;

        for (int i = 0; i < request.getProductos().size(); i++) {
            ProductOrderDto productOrder = request.getProductos().get(i);
            ProductResponseDto product = validatedProducts.get(i);

            DetallePedidoCreateRequestDto detalleRequest = new DetallePedidoCreateRequestDto();
            detalleRequest.setProductoId(productOrder.getProductoId());
            detalleRequest.setCantidad(productOrder.getCantidad());
            detalleRequest.setPrecioUnitario(product.getPrice());

            pedido = pedidoClient.addDetalle(pedido.getId(), detalleRequest);

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(productOrder.getCantidad()));
            totalCalculado = totalCalculado.add(subtotal);

            OrderCompositionResponseDto.ProductDetailDto detail = new OrderCompositionResponseDto.ProductDetailDto();
            detail.setProductId(product.getId());
            detail.setProductName(product.getName());
            detail.setPrecioUnitario(product.getPrice());
            detail.setCantidad(productOrder.getCantidad());
            detail.setSubtotal(subtotal);
            productDetails.add(detail);
        }

        OrderCompositionResponseDto response = new OrderCompositionResponseDto();
        response.setPedidoId(pedido.getId());
        response.setFechaPedido(pedido.getFechaPedido());
        response.setTotal(totalCalculado);
        response.setStatus("CREATED");
        response.setProductosDetalle(productDetails);

        OrderCompositionResponseDto.UserSummaryDto userSummary = new OrderCompositionResponseDto.UserSummaryDto();
        userSummary.setId(user.getId());
        userSummary.setName(user.getName());
        userSummary.setEmail(user.getEmail());
        response.setUsuario(userSummary);

        return response;
    }

    @Override
    public OrderCompositionResponseDto getOrderComposition(Long pedidoId) {
        PedidoResponseDto pedido = pedidoClient.getPedidoById(pedidoId);
        UserResponseDto user = userClient.getUserById(pedido.getUserId());

        List<OrderCompositionResponseDto.ProductDetailDto> productDetails = pedido.getDetalles().stream()
                .map(detalle -> {
                    ProductResponseDto product = productClient.getProductById(detalle.getProductoId());

                    OrderCompositionResponseDto.ProductDetailDto detail = new OrderCompositionResponseDto.ProductDetailDto();
                    detail.setProductId(product.getId());
                    detail.setProductName(product.getName());
                    detail.setPrecioUnitario(detalle.getPrecioUnitario());
                    detail.setCantidad(detalle.getCantidad());
                    detail.setSubtotal(detalle.getSubtotal());

                    return detail;
                }).toList();

        OrderCompositionResponseDto response = new OrderCompositionResponseDto();
        response.setPedidoId(pedido.getId());
        response.setFechaPedido(pedido.getFechaPedido());
        response.setTotal(pedido.getTotal());
        response.setStatus(pedido.getActive() ? "ACTIVE" : "INACTIVE");
        response.setProductosDetalle(productDetails);

        OrderCompositionResponseDto.UserSummaryDto userSummary = new OrderCompositionResponseDto.UserSummaryDto();
        userSummary.setId(user.getId());
        userSummary.setName(user.getName());
        userSummary.setEmail(user.getEmail());
        response.setUsuario(userSummary);

        return response;
    }

    @Override
    public List<OrderCompositionResponseDto> getAllOrdersComposition() {
        return pedidoClient.getAllPedidos().stream()
                .map(pedido -> getOrderComposition(pedido.getId()))
                .toList();
    }

    @Override
    public List<OrderCompositionResponseDto> getOrdersByUser(Long userId) {
        return pedidoClient.getAllPedidos().stream()
                .filter(pedido -> pedido.getUserId().equals(userId))
                .map(pedido -> getOrderComposition(pedido.getId()))
                .toList();
    }

    @Override
    @Transactional
    public void cancelOrder(Long pedidoId) {
        pedidoClient.getPedidoById(pedidoId);
        pedidoClient.deletePedido(pedidoId);
    }

    @Override
    public OrderCompositionResponseDto getOrderSummary(Long pedidoId) {
        return getOrderComposition(pedidoId);
    }
}
