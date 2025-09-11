package cd.dev.pedidoms.controller;

import cd.dev.pedidoms.model.dto.*;
import cd.dev.pedidoms.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public List<PedidoResponseDto> listAll() {
        return pedidoService.listAll();
    }

    @GetMapping("/{id}")
    public PedidoResponseDto findById(@PathVariable Long id) {
        return pedidoService.findById(id);
    }

    @PostMapping
    public PedidoResponseDto create(@Valid @RequestBody PedidoCreateRequestDto request) {
        return pedidoService.save(request);
    }

    @PostMapping("/{pedidoId}/detalles")
    public PedidoResponseDto addDetalle(
            @PathVariable Long pedidoId,
            @Valid @RequestBody DetallePedidoCreateRequestDto detalleDto) {
        return pedidoService.addDetalle(pedidoId, detalleDto);
    }

    @PutMapping("/{pedidoId}/detalles/{detalleId}")
    public PedidoResponseDto updateDetalle(
            @PathVariable Long pedidoId,
            @PathVariable Long detalleId,
            @Valid @RequestBody DetallePedidoUpdateRequestDto request) {
        return pedidoService.updateDetalle(pedidoId, detalleId, request);
    }

    @DeleteMapping("/{pedidoId}/detalles/{detalleId}")
    public PedidoResponseDto removeDetalle(
            @PathVariable Long pedidoId,
            @PathVariable Long detalleId) {
        return pedidoService.removeDetalle(pedidoId, detalleId);
    }

    @PutMapping("/{id}")
    public PedidoResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody PedidoUpdateRequestDto request) {
        return pedidoService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        pedidoService.deleteById(id);
    }
}