package cd.dev.pedidoms.service;


import cd.dev.pedidoms.model.dto.*;

import java.util.List;

public interface PedidoService {
    List<PedidoResponseDto> listAll();
    PedidoResponseDto findById(Long id);
    PedidoResponseDto save(PedidoCreateRequestDto request);
    PedidoResponseDto update(Long id, PedidoUpdateRequestDto request);
    void deleteById(Long id);
    PedidoResponseDto removeDetalle(Long pedidoId, Long detalleId);
    PedidoResponseDto updateDetalle(Long pedidoId, Long detalleId, DetallePedidoUpdateRequestDto request);
    PedidoResponseDto addDetalle(Long pedidoId, DetallePedidoCreateRequestDto detalleDto);
}