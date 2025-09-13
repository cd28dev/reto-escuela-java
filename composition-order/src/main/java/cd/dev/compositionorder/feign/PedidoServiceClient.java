package cd.dev.compositionorder.feign;

import cd.dev.compositionorder.dto.external.DetallePedidoCreateRequestDto;
import cd.dev.compositionorder.dto.external.PedidoCreateRequestDto;
import cd.dev.compositionorder.dto.external.PedidoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "pedido-service", url = "${services.pedido-ms.url}")
public interface PedidoServiceClient {

    @GetMapping("/api/pedidos/{id}")
    PedidoResponseDto getPedidoById(@PathVariable Long id);

    @GetMapping("/api/pedidos")
    List<PedidoResponseDto> getAllPedidos();

    @PostMapping("/api/pedidos")
    PedidoResponseDto createPedido(@RequestBody PedidoCreateRequestDto request);

    @PostMapping("/api/pedidos/{pedidoId}/detalles")
    PedidoResponseDto addDetalle(@PathVariable Long pedidoId,
                                 @RequestBody DetallePedidoCreateRequestDto detalleDto);

    @DeleteMapping("/api/pedidos/{id}")
    void deletePedido(@PathVariable Long id);
}
